package io.wso2.android.api_authenticator.sdk.data.token

import io.wso2.android.api_authenticator.sdk.models.state.TokenState

/**
 * Interface which has the methods to manage the token data store.
 */
internal interface TokenDataStore {
    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    suspend fun saveTokenState(tokenState: TokenState)

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    suspend fun getTokenState(): TokenState?

    /**
     * Clear the tokens from the token data store.
     */
    suspend fun clearTokens()
}
