// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.appconfiguration.config.implementation;

import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.List;

import org.springframework.util.StringUtils;

import com.azure.core.exception.HttpResponseException;
import com.azure.core.http.HttpHeaderName;
import com.azure.core.http.MatchConditions;
import com.azure.core.http.rest.PagedResponse;
import com.azure.data.appconfiguration.ConfigurationAsyncClient;
import com.azure.data.appconfiguration.models.ConfigurationSetting;
import com.azure.data.appconfiguration.models.ConfigurationSnapshot;
import com.azure.data.appconfiguration.models.SettingSelector;
import com.azure.data.appconfiguration.models.SnapshotComposition;
import com.azure.spring.cloud.appconfiguration.config.implementation.http.policy.TracingInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client for connecting to App Configuration when multiple replicas are in use.
 */
class AppConfigurationReplicaClient {

    private final String endpoint;

    private final ConfigurationAsyncClient client;

    private Instant backoffEndTime;

    private int failedAttempts;

    private final TracingInfo tracingInfo;

    /**
     * Holds Configuration Client and info needed to manage backoff.
     * @param endpoint client endpoint
     * @param client Configuration Client to App Configuration store
     */
    AppConfigurationReplicaClient(String endpoint, ConfigurationAsyncClient client, TracingInfo tracingInfo) {
        this.endpoint = endpoint;
        this.client = client;
        this.backoffEndTime = Instant.now().minusMillis(1);
        this.failedAttempts = 0;
        this.tracingInfo = tracingInfo;
    }

    /**
     * @return backOffEndTime
     */
    Instant getBackoffEndTime() {
        return backoffEndTime;
    }

    /**
     * Updates the backoff time and increases the number of failed attempts.
     * @param backoffEndTime next time this client can be used.
     */
    void updateBackoffEndTime(Instant backoffEndTime) {
        this.backoffEndTime = backoffEndTime;
        this.failedAttempts += 1;
    }

    /**
     * @return number of failed attempts
     */
    int getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * @return endpoint
     */
    String getEndpoint() {
        return endpoint;
    }

    /**
     * Gets the Configuration Setting for the given config store that match the Setting Selector criteria. Follows
     * retry-after-ms header.
     *
     * @param key String value of the watch key
     * @param label String value of the watch key, use \0 for null.
     * @return The first returned configuration.
     */
    Mono<ConfigurationSetting> getWatchKey(String key, String label)
        throws HttpResponseException {
        try {
            return client.getConfigurationSetting(key, label).map(setting -> processConfigurationSetting(setting));
        } catch (HttpResponseException e) {
            if (e.getResponse() != null) {
                int statusCode = e.getResponse().getStatusCode();

                if (statusCode == 429 || statusCode == 408 || statusCode >= 500) {
                    throw new AppConfigurationStatusException(e.getMessage(), e.getResponse(), e.getValue());
                }
            }
            throw e;
        } catch (UncheckedIOException e) {
            throw new AppConfigurationStatusException(e.getMessage(), null, null);
        }
    }

    /**
     * Gets a list of Configuration Settings from the given config store that match the Setting Selector criteria.
     *
     * @param settingSelector Information on which setting to pull. i.e. number of results, key value...
     * @return List of Configuration Settings.
     */
    Flux<ConfigurationSetting> listSettings(SettingSelector settingSelector)
        throws HttpResponseException {
        try {
            return client.listConfigurationSettings(settingSelector)
                .map(setting -> processConfigurationSetting(setting));
        } catch (HttpResponseException e) {
            if (e.getResponse() != null) {
                int statusCode = e.getResponse().getStatusCode();

                if (statusCode == 429 || statusCode == 408 || statusCode >= 500) {
                    throw new AppConfigurationStatusException(e.getMessage(), e.getResponse(), e.getValue());
                }
            }
            throw e;
        } catch (UncheckedIOException e) {
            throw new AppConfigurationStatusException(e.getMessage(), null, null);
        }
    }

    List<ConfigurationSetting> listSettingSnapshot(String snapshotName) {
        try {
            ConfigurationSnapshot snapshot = client.getSnapshot(snapshotName).block();
            if (!SnapshotComposition.KEY.equals(snapshot.getSnapshotComposition())) {
                throw new IllegalArgumentException("Snapshot " + snapshotName + " needs to be of type Key.");
            }

            return client.listConfigurationSettingsForSnapshot(snapshotName)
                .map(setting -> processConfigurationSetting(setting)).collectList().block();
        } catch (HttpResponseException e) {
            if (e.getResponse() != null) {
                int statusCode = e.getResponse().getStatusCode();

                if (statusCode == 429 || statusCode == 408 || statusCode >= 500) {
                    throw new AppConfigurationStatusException(e.getMessage(), e.getResponse(), e.getValue());
                }
            }
            throw e;
        } catch (UncheckedIOException e) {
            throw new AppConfigurationStatusException(e.getMessage(), null, null);
        }
    }

    Flux<MatchConditions> getPagedEtags(SettingSelector settingSelector) {
        return client.listConfigurationSettings(settingSelector).byPage()
            .map(pagedResponse -> new MatchConditions().setIfNoneMatch(
                pagedResponse.getHeaders().getValue(HttpHeaderName.ETAG)));
    }

    Flux<PagedResponse<ConfigurationSetting>> checkWatchKeys(SettingSelector settingSelector) {
        return client.listConfigurationSettings(settingSelector).byPage().filter(pagedResponse -> {
            return pagedResponse.getStatusCode() != 304;
        });
    }

    /**
     * Update the sync token for a client store.
     * @param syncToken the sync token.
     */
    void updateSyncToken(String syncToken) {
        if (StringUtils.hasText(syncToken)) {
            client.updateSyncToken(syncToken);
        }
    }

    TracingInfo getTracingInfo() {
        return tracingInfo;
    }

    ConfigurationSetting processConfigurationSetting(ConfigurationSetting setting) {
        this.failedAttempts = 0;
        return NormalizeNull.normalizeNullLabel(setting);
    }

}
