package io.wso2.android.api_authenticator.sdk.provider.providers.token.impl

import android.content.Context
import io.wso2.android.api_authenticator.sdk.provider.provider_managers.token.TokenProviderManager
import io.wso2.android.api_authenticator.sdk.provider.providers.token.TokenProvider
import java.lang.ref.WeakReference

/**
 * The [TokenProviderImpl] class provides the functionality to get the tokens, validate the tokens,
 * refresh the tokens, and clear the tokens.
 *
 * @property tokenProviderManager The [TokenProviderManager] instance
 */
internal class TokenProviderImpl private constructor(
    private val tokenProviderManager: TokenProviderManager
) : TokenProvider {
    companion object {
        /**
         * Instance of the [TokenProviderImpl] that will be used throughout the application
         */
        private var tokenProviderImplInstance: WeakReference<TokenProviderImpl> =
            WeakReference(null)

        /**
         * Initialize the [TokenProviderImpl] instance and return the instance.
         *
         * @param tokenProviderManager The [TokenProviderManager] instance
         *
         * @return Initialized [TokenProviderImpl] instance
         */
        fun getInstance(tokenProviderManager: TokenProviderManager): TokenProviderImpl {
            var tokenProvider = tokenProviderImplInstance.get()
            if (tokenProvider == null) {
                tokenProvider = TokenProviderImpl(tokenProviderManager)
                tokenProviderImplInstance = WeakReference(tokenProvider)
            }
            return tokenProvider
        }
    }

    /**
     * Get the access token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token [String]
     */
    override suspend fun getAccessToken(context: Context): String? =
        tokenProviderManager.getAccessToken(context)

    /**
     * Get the refresh token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The refresh token [String]
     */
    override suspend fun getRefreshToken(context: Context): String? =
        tokenProviderManager.getRefreshToken(context)

    /**
     * Get the ID token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The ID token [String]
     */
    override suspend fun getIDToken(context: Context): String? =
        tokenProviderManager.getIDToken(context)

    /**
     * Get the decoded ID token
     *
     * @param context The [Context] instance.
     *
     * @return The decoded ID token [String]
     */
    override suspend fun getDecodedIDToken(context: Context): LinkedHashMap<String, Any> =
        tokenProviderManager.getDecodedIDToken(context)

    /**
     * Get the access token expiration time from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token expiration time [Long]
     */
    override suspend fun getAccessTokenExpirationTime(context: Context): Long? =
        tokenProviderManager.getAccessTokenExpirationTime(context)

    /**
     * Get the scope from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The scope [String]
     */
    override suspend fun getScope(context: Context): String? =
        tokenProviderManager.getScope(context)


    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     *
     * **Here we are not calling the introspection endpoint to validate the access token!.
     * We are checking the expiration time of the access token and
     * if the access token is null or empty.**
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    override suspend fun validateAccessToken(context: Context): Boolean? =
        tokenProviderManager.validateAccessToken(context)

    /**
     * Perform refresh token grant. This method will perform the refresh token grant and save the
     * updated token state in the data store. If refresh token grant fails, it will throw an
     * Exception.
     *
     * @param context The [Context] instance.
     */
    override suspend fun performRefreshTokenGrant(context: Context) =
        tokenProviderManager.performRefreshTokenGrant(context)

    /**
     * Perform an action with fresh tokens. This method will perform the action with fresh tokens
     * and save the updated token state in the data store. Developer can directly use this method
     * perform an action with fresh tokens, without worrying about refreshing the tokens. If this
     * action fails, it will throw an Exception.
     *
     * @param context The [Context] instance.
     * @param action The action to perform.
     */
    override suspend fun performAction(
        context: Context,
        action: suspend (String?, String?) -> Unit
    ) = tokenProviderManager.performAction(context, action)

    /**
     * Clear the tokens from the token data store. This method will clear the tokens from the
     * data store. After calling this method, developer needs to perform the authorization flow
     * again to get the tokens.
     *
     * @param context The [Context] instance.
     */
    override suspend fun clearTokens(context: Context): Unit? =
        tokenProviderManager.clearTokens(context)
}
