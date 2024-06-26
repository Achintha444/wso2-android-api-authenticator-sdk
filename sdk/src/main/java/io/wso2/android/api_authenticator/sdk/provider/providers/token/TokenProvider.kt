package io.wso2.android.api_authenticator.sdk.provider.providers.token

import android.content.Context

/**
 * The [TokenProvider] interface provides the functionality to get the tokens, validate the tokens,
 * refresh the tokens, and clear the tokens.
 */
interface TokenProvider {
    /**
     * Get the access token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token [String]
     */
    suspend fun getAccessToken(context: Context): String?

    /**
     * Get the refresh token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The refresh token [String]
     */
    suspend fun getRefreshToken(context: Context): String?

    /**
     * Get the ID token from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The ID token [String]
     */
    suspend fun getIDToken(context: Context): String?

    /**
     * Get the decoded ID token
     *
     * @param context The [Context] instance.
     *
     * @return The decoded ID token [String]
     */
    suspend fun getDecodedIDToken(context: Context): LinkedHashMap<String, Any>

    /**
     * Get the access token expiration time from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The access token expiration time [Long]
     */
    suspend fun getAccessTokenExpirationTime(context: Context): Long?

    /**
     * Get the scope from the token.
     *
     * @param context The [Context] instance.
     *
     * @return The scope [String]
     */
    suspend fun getScope(context: Context): String?


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
    suspend fun validateAccessToken(context: Context): Boolean?

    /**
     * Perform refresh token grant. This method will perform the refresh token grant and save the
     * updated token state in the data store. If refresh token grant fails, it will throw an
     * Exception.
     *
     * @param context The [Context] instance.
     */
    suspend fun performRefreshTokenGrant(context: Context)

    /**
     * Perform an action with fresh tokens. This method will perform the action with fresh tokens
     * and save the updated token state in the data store. Developer can directly use this method
     * perform an action with fresh tokens, without worrying about refreshing the tokens. If this
     * action fails, it will throw an Exception.
     *
     * @param context The [Context] instance.
     * @param action The action to perform.
     */
    suspend fun performAction(
        context: Context,
        action: suspend (String?, String?) -> Unit
    )

    /**
     * Clear the tokens from the token data store. This method will clear the tokens from the
     * data store. After calling this method, developer needs to perform the authorization flow
     * again to get the tokens.
     *
     * @param context The [Context] instance.
     */
    suspend fun clearTokens(context: Context): Unit?
}
