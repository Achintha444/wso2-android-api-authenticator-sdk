package io.wso2.android.api_authenticator.sdk.core.di

import io.wso2.android.api_authenticator.sdk.data.token.TokenDataStoreFactory

/**
 * Dependency container for the [TokenManagerImpl] class.
 */
internal object TokenManagerImplContainer {
    /**
     * Returns an instance of the [TokenDataStoreFactory] class.
     *
     * @return [TokenDataStoreFactory] instance.
     */
    internal fun getTokenDataStoreFactory(): TokenDataStoreFactory {
        return TokenDataStoreFactory
    }
}