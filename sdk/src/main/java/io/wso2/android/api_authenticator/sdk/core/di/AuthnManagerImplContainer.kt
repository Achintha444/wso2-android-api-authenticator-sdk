package io.wso2.android.api_authenticator.sdk.core.di

import io.wso2.android.api_authenticator.sdk.core.managers.authn.impl.AuthnManagerImpl
import io.wso2.android.api_authenticator.sdk.core.managers.authn.impl.AuthnManagerImplRequestBuilder
import io.wso2.android.api_authenticator.sdk.core.managers.flow.FlowManager
import io.wso2.android.api_authenticator.sdk.core.managers.flow.impl.FlowManagerImpl
import io.wso2.android.api_authenticator.sdk.models.http_client.LessSecureHttpClient
import io.wso2.android.api_authenticator.sdk.models.http_client.http_client_builder.HttpClientBuilder
import okhttp3.OkHttpClient

/**
 * Dependency Injection container for the [AuthnManagerImpl] class.
 */
internal object AuthnManagerImplContainer {
    /**
     * Returns an instance of the [OkHttpClient] class, based on the given parameters.
     *
     * @property isDevelopment The flag to check whether the app is in development mode or not.
     * If true, the [LessSecureHttpClient] instance will be returned. Otherwise, the default
     * [OkHttpClient] instance will be returned. Default value is `false`. It is not recommended to
     * keep this value as `true` in production environment.
     *
     * @return [OkHttpClient] instance.
     */
    internal fun getClient(isDevelopment: Boolean?): OkHttpClient =
        HttpClientBuilder.getHttpClientInstance(isDevelopment)

    /**
     * Returns an instance of the [AuthnManagerImplRequestBuilder] class.
     *
     * @return [AuthnManagerImplRequestBuilder] instance.
     */
    internal fun getAuthenticationCoreRequestBuilder(): AuthnManagerImplRequestBuilder =
        AuthnManagerImplRequestBuilder

    /**
     * Returns an instance of the [FlowManager] class.
     *
     * @return [FlowManager] instance.
     *
     */
    internal fun getFlowManager(): FlowManager = FlowManagerImpl.getInstance()
}
