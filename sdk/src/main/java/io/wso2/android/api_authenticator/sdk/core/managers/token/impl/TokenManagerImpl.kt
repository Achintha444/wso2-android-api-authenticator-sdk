package io.wso2.android.api_authenticator.sdk.core.managers.token.impl

import android.content.Context
import io.wso2.android.api_authenticator.sdk.core.di.TokenManagerImplContainer
import io.wso2.android.api_authenticator.sdk.core.managers.token.TokenManager
import io.wso2.android.api_authenticator.sdk.data.token.TokenDataStore
import io.wso2.android.api_authenticator.sdk.models.state.TokenState

/**
 * Use to manage the tokens.
 *
 * @property context The [Context] instance.
 */
internal class TokenManagerImpl internal constructor(private val context: Context) : TokenManager {
    // Get the token data store
    private val tokenDataStore: TokenDataStore =
        TokenManagerImplContainer.getTokenDataStoreFactory().getTokenDataStore(context)

    /**
     * Save the [TokenState] to the data store.
     *
     * @param tokenState The [TokenState] instance.
     */
    override suspend fun saveTokenState(tokenState: TokenState): Unit =
        tokenDataStore.saveTokenState(tokenState)

    /**
     * Get the [TokenState] from the data store.
     *
     * @return The [TokenState] instance.
     */
    override suspend fun getTokenState(): TokenState? = tokenDataStore.getTokenState()

    /**
     * Get the access token from the token data store.
     *
     * @return The access token [String]
     */
    override suspend fun getAccessToken(): String? =
        getTokenState()?.getAppAuthState()?.accessToken

    /**
     * Get the refresh token from the token data store.
     *
     * @return The refresh token [String]
     */
    override suspend fun getRefreshToken(): String? =
        getTokenState()?.getAppAuthState()?.refreshToken

    /**
     * Get the ID token from the token data store.
     *
     * @return The ID token [String]
     */
    override suspend fun getIDToken(): String? =
        getTokenState()?.getAppAuthState()?.idToken

    /**
     * Get the access token expiration time from the token data store.
     *
     * @return The access token expiration time [Long]
     */
    override suspend fun getAccessTokenExpirationTime(): Long? =
        getTokenState()?.getAppAuthState()?.accessTokenExpirationTime

    /**
     * Get the scope from the token data store.
     *
     * @return The scope [String]
     */
    override suspend fun getScope(): String? =
        getTokenState()?.getAppAuthState()?.scope

    /**
     * Clear the tokens from the token data store.*
     */
    override suspend fun clearTokens(): Unit =
        tokenDataStore.clearTokens()

    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     * **Here we are not calling the introspection endpoint to validate the access token!**
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    override suspend fun validateAccessToken(): Boolean {
        return getTokenState()?.getAppAuthState()?.isAuthorized == true
    }
}
