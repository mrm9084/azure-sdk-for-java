// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.cloud.config.implementation.stores;

import java.net.URI;
import java.time.Duration;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretAsyncClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.spring.cloud.config.KeyVaultCredentialProvider;
import com.azure.spring.cloud.config.KeyVaultSecretProvider;
import com.azure.spring.cloud.config.SecretClientBuilderSetup;

/**
 * Client for connecting to and getting secrets from a Key Vault
 */
public final class AppConfigurationSecretClientManager {

    private SecretAsyncClient secretClient;

    private final SecretClientBuilderSetup keyVaultClientProvider;

    private final String endpoint;

    private final TokenCredential tokenCredential;

    private final KeyVaultSecretProvider keyVaultSecretProvider;

    /**
     * Creates a Client for connecting to Key Vault
     * @param endpoint Key Vault endpoint
     * @param tokenCredentialProvider optional provider of the Token Credential for connecting to Key Vault
     * @param keyVaultClientProvider optional provider for overriding the Key Vault Client
     * @param keyVaultSecretProvider optional provider for providing Secrets instead of connecting to Key Vault
     */
    public AppConfigurationSecretClientManager(String endpoint, KeyVaultCredentialProvider tokenCredentialProvider,
        SecretClientBuilderSetup keyVaultClientProvider, KeyVaultSecretProvider keyVaultSecretProvider) {
        this.endpoint = endpoint;
        if (tokenCredentialProvider != null) {
            this.tokenCredential = tokenCredentialProvider.getKeyVaultCredential(endpoint);
        } else {
            this.tokenCredential = null;
        }
        this.keyVaultClientProvider = keyVaultClientProvider;
        this.keyVaultSecretProvider = keyVaultSecretProvider;
    }

    AppConfigurationSecretClientManager build() {
        SecretClientBuilder builder = getBuilder();

        if (tokenCredential != null) {
            // User Provided Token Credential
            builder.credential(tokenCredential);
        } else {
            // System Assigned Identity.
            builder.credential(new ManagedIdentityCredentialBuilder().build());
        }
        builder.vaultUrl(endpoint);

        if (keyVaultClientProvider != null) {
            keyVaultClientProvider.setup(builder, endpoint);
        }

        secretClient = builder.buildAsyncClient();

        return this;
    }

    /**
     * Gets the specified secret using the Secret Identifier
     *
     * @param secretIdentifier The Secret Identifier to Secret
     * @param timeout How long it waits for a response from Key Vault
     * @return Secret values that matches the secretIdentifier
     */
    public KeyVaultSecret getSecret(URI secretIdentifier, int timeout) {
        
        if (tokenCredential != null && keyVaultSecretProvider != null) {
            throw new IllegalArgumentException("A Key Vault can't have both a token credential and a KeyVaultSecretProvider.");
        }
        
        if (secretClient == null && keyVaultSecretProvider == null) {
            build();
        } else if (keyVaultSecretProvider != null) {
            return new KeyVaultSecret(null, keyVaultSecretProvider.getSecret(secretIdentifier.getRawPath()));
        }
        

        String[] tokens = secretIdentifier.getPath().split("/");

        String name = (tokens.length >= 3 ? tokens[2] : null);
        String version = (tokens.length >= 4 ? tokens[3] : null);
        return secretClient.getSecret(name, version).block(Duration.ofSeconds(timeout));
    }

    SecretClientBuilder getBuilder() {
        return new SecretClientBuilder();
    }

}
