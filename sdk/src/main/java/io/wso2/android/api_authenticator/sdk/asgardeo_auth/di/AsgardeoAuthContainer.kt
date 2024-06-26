package io.wso2.android.api_authenticator.sdk.asgardeo_auth.di

import io.wso2.android.api_authenticator.sdk.asgardeo_auth.AsgardeoAuth
import io.wso2.android.api_authenticator.sdk.core.core_types.authentication.AuthenticationCoreDef
import io.wso2.android.api_authenticator.sdk.core.core_types.authentication.impl.AuthenticationCore
import io.wso2.android.api_authenticator.sdk.core.core_types.native_authentication_handler.NativeAuthenticationHandlerCoreDef
import io.wso2.android.api_authenticator.sdk.core.core_types.native_authentication_handler.impl.NativeAuthenticationHandlerCore
import io.wso2.android.api_authenticator.sdk.core_config.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core_config.di.AuthenticationCoreConfigProviderImplContainer
import io.wso2.android.api_authenticator.sdk.core_config.providers.authentication_core_config_provider.AuthenticationCoreConfigProvider
import io.wso2.android.api_authenticator.sdk.core_config.providers.authentication_core_config_provider.impl.AuthenticationCoreConfigProviderImpl

/**
 * Dependency Injection container for the [AsgardeoAuth] class.
 */
internal object AsgardeoAuthContainer {

    /**
     * Get the instance of the [AuthenticationCoreConfigProvider].
     *
     * @param authenticationCoreConfig Configuration of the [AuthenticationCoreDef]
     *
     * @return [AuthenticationCoreConfigProvider] instance
     */
    internal fun getAuthenticationCoreConfigProvider(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticationCoreConfigProvider = AuthenticationCoreConfigProviderImpl.getInstance(
        authenticationCoreConfig,
        AuthenticationCoreConfigProviderImplContainer.getAuthenticationCoreConfigManager(
            authenticationCoreConfig
        )
    )

    /**
     * Get the instance of the [AuthenticationCoreDef].
     *
     * @param authenticationCoreConfigProvider Provider to update the [AuthenticationCoreConfig] based on the discovery response [AuthenticationCoreConfigProvider]
     *
     * @return [AuthenticationCoreDef] instance
     */
    internal fun getAuthenticationCoreDef(
        authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
    ): AuthenticationCoreDef = AuthenticationCore.getInstance(authenticationCoreConfigProvider)

    /**
     * Get the instance of the [NativeAuthenticationHandlerCoreDef].
     *
     * @param authenticationCoreConfigProvider Provider to update the [AuthenticationCoreConfig] based on the discovery response [AuthenticationCoreConfigProvider]
     *
     * @return [NativeAuthenticationHandlerCoreDef] instance
     */
    internal fun getNativeAuthenticationHandlerCoreDef(
        authenticationCoreConfigProvider: AuthenticationCoreConfigProvider
    ): NativeAuthenticationHandlerCoreDef =
        NativeAuthenticationHandlerCore.getInstance(authenticationCoreConfigProvider)
}
