package io.wso2.android.api_authenticator.sdk.core.di

import io.wso2.android.api_authenticator.sdk.core.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core.managers.authenticator.AuthenticatorManager
import io.wso2.android.api_authenticator.sdk.core.managers.authenticator.impl.AuthenticatorManagerImpl
import io.wso2.android.api_authenticator.sdk.core.managers.flow.impl.FlowManagerImpl

/**
 * Dependency Injection container for the [FlowManagerImpl] class.
 */
internal object FlowManagerImplContainer {
    /**
     * Returns an instance of the [AuthenticatorManager] object, based on the given parameters.
     *
     * @property authenticationCoreConfig The [AuthenticationCoreConfig] instance.
     *
     * @return [AuthenticatorManager] instance.
     */
    internal fun getAuthenticatorManagerInstance(
        authenticationCoreConfig: AuthenticationCoreConfig
    ): AuthenticatorManager = AuthenticatorManagerImpl.getInstance(
        AuthenticatorManagerImplContainer.getClient(
            authenticationCoreConfig.getIsDevelopment()
        ),
        AuthenticatorManagerImplContainer.getAuthenticatorTypeFactory(),
        AuthenticatorManagerImplContainer.getAuthenticatorManagerImplRequestBuilder(),
        AuthenticatorManagerImplContainer.getAuthnUrl(authenticationCoreConfig.getAuthnUrl())
    )
}
