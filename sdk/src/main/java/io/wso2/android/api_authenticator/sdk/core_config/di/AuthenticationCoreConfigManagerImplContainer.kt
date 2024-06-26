package io.wso2.android.api_authenticator.sdk.core_config.di

import io.wso2.android.api_authenticator.sdk.core_config.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core_config.AuthenticationCoreConfigFactory
import io.wso2.android.api_authenticator.sdk.core_config.managers.authentication_core_config_manager.impl.AuthenticationCoreConfigManagerImpl
import io.wso2.android.api_authenticator.sdk.core_config.managers.discovery_manager.DiscoveryManager
import io.wso2.android.api_authenticator.sdk.core_config.managers.discovery_manager.impl.DiscoveryManagerImpl

/**
 * Container for [AuthenticationCoreConfigManagerImpl]
 */
object AuthenticationCoreConfigManagerImplContainer {

    /**
     * Provide [AuthenticationCoreConfigFactory] instance
     *
     * @return [AuthenticationCoreConfigFactory] instance
     */
    internal fun getAuthenticationCoreConfigFactory(): AuthenticationCoreConfigFactory =
        AuthenticationCoreConfigFactory

    /**
     * Provide [DiscoveryManager] instance
     *
     * @param authenticationCoreConfig [AuthenticationCoreConfig] instance
     *
     * @return [DiscoveryManager] instance
     */
    internal fun getDiscoveryManager(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): DiscoveryManager = DiscoveryManagerImpl.getInstance(
        client = DiscoveryManagerImplContainer
            .getClient(authenticationCoreConfig.getIsDevelopment()),
        discoveryManagerImplRequestBuilder = DiscoveryManagerImplContainer
            .getDiscoveryManagerImplRequestBuilder()
    )
}
