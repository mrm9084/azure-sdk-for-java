package com.azure.spring.cloud.config.implementation;

import com.azure.data.appconfiguration.ConfigurationServiceVersion;
import com.azure.spring.cloud.service.implementation.appconfiguration.ConfigurationClientProperties;

public class AppConfigurationProviderGlobalProperties implements ConfigurationClientProperties {

    @Override
    public ClientOptions getClient() {
        return null;
    }

    @Override
    public ProxyOptions getProxy() {
        return null;
    }

    @Override
    public TokenCredentialOptions getCredential() {
        return null;
    }

    @Override
    public ProfileOptions getProfile() {
        return null;
    }

    @Override
    public RetryOptions getRetry() {
        return null;
    }

    @Override
    public String getConnectionString() {
        return null;
    }

    @Override
    public String getEndpoint() {
        return null;
    }

    @Override
    public ConfigurationServiceVersion getServiceVersion() {
        return null;
    }

}
