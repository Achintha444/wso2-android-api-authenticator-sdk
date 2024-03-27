package io.wso2.android.api_authenticator.sdk.provider.di

import io.wso2.android.api_authenticator.sdk.core.AuthenticationCoreDef
import io.wso2.android.api_authenticator.sdk.provider.provider_managers.authentication.AuthenticationProviderManager
import io.wso2.android.api_authenticator.sdk.provider.provider_managers.authentication.impl.AuthenticationProviderManagerImpl
import io.wso2.android.api_authenticator.sdk.provider.providers.authentication.impl.AuthenticationProviderImpl

/**
 * Dependency Injection container for the [AuthenticationProviderImpl] class.
 */
internal object AuthenticationProviderImplContainer {
    /**
     * Get the instance of the [AuthenticationProviderManager].
     *
     * @param authenticationCore [AuthenticationCoreDef] instance
     *
     * @return [AuthenticationProviderManager] instance
     */
    internal fun getAuthenticationProviderManager(authenticationCore: AuthenticationCoreDef)
            : AuthenticationProviderManager =
        AuthenticationProviderManagerImpl.getInstance(
            authenticationCore,
            AuthenticationProviderManagerImplContainer.getAuthenticationStateProviderManager(
                authenticationCore
            ),
            AuthenticationProviderManagerImplContainer.getAuthenticationHandlerProviderManager(
                authenticationCore
            )
        )
}