package io.wso2.android.api_authenticator.sdk.core

import android.content.Context
import io.wso2.android.api_authenticator.sdk.models.auth_params.AuthParams
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.AuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlow
import io.wso2.android.api_authenticator.sdk.models.exceptions.AuthnManagerException
import io.wso2.android.api_authenticator.sdk.models.state.TokenState
import java.io.IOException

/**
 * Authentication core class interface which has the core functionality of the Authenticator SDK.
 */
interface AuthenticationCoreDef {
    /**
     * Authorize the application.
     * This method will call the authorization endpoint and get the authenticators available for the
     * first step in the authentication flow.
     */
    suspend fun authorize(): AuthenticationFlow?

    /**
     * Send the authentication parameters to the authentication endpoint and get the next step of the
     * authentication flow. If the authentication flow has only one step, this method will return
     * the success response of the authentication flow if the authentication is successful.
     *
     * @param authenticatorType Authenticator type of the selected authenticator
     * @param authenticatorParameters Authenticator parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     *
     * @return [AuthenticationFlow] with the next step of the authentication flow
     */
    suspend fun authenticate(
        authenticatorType: AuthenticatorType,
        authenticatorParameters: LinkedHashMap<String, String>
    ): AuthenticationFlow?

    /**
     * Exchange the authorization code for the access token.
     *
     * @param authorizationCode Authorization code
     * @param context Context of the application
     *
     * @return token state [TokenState]
     */
    suspend fun exchangeAuthorizationCode(
        authorizationCode: String,
        context: Context,
    ): TokenState?

    /**
     * Perform the refresh token grant.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     *
     *
     * @return updated [TokenState] instance.
     */
    suspend fun performRefreshTokenGrant(
        context: Context,
        tokenState: TokenState,
    ): TokenState?

    /**
     * Perform an action with fresh tokens.
     *
     * @param context The [Context] instance.
     * @param tokenState The [TokenState] instance.
     * @param action The action to perform.
     *
     * @return Updated [TokenState] instance.
     */
    suspend fun performActionWithFreshTokens(
        context: Context,
        tokenState: TokenState,
        action: suspend (String?, String?) -> Unit
    ): TokenState?

    /**
     * Save the [TokenState] to the data store.
     *
     * @param context Context of the application
     * @param tokenState The [TokenState] instance.
     */
    suspend fun saveTokenState(context: Context, tokenState: TokenState): Unit?

    /**
     * Get the [TokenState] from the data store.
     *
     * @param context Context of the application
     *
     * @param context Context of the application
     */
    suspend fun getTokenState(context: Context): TokenState?

    /**
     * Get the access token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The access token [String]
     */
    suspend fun getAccessToken(context: Context): String?

    /**
     * Get the refresh token from the token data store.
     *
     * @param context Context of the application
     *
     * @return The refresh token [String]
     */
    suspend fun getRefreshToken(context: Context): String?

    /**
     * Get the ID token from the token data store.
     *
     * @return The ID token [String]
     */
    suspend fun getIDToken(context: Context): String?

    /**
     * Get the access token expiration time from the token data store.
     *
     * @param context Context of the application
     *
     * @return The access token expiration time [Long]
     */
    suspend fun getAccessTokenExpirationTime(context: Context): Long?

    /**
     * Get the scope from the token data store.
     *
     * @param context Context of the application
     *
     * @return The scope [String]
     */
    suspend fun getScope(context: Context): String?

    /**
     * Clear the tokens from the token data store.
     *
     * @param context Context of the application
     */
    suspend fun clearTokens(context: Context): Unit?

    /**
     * Validate the access token, by checking the expiration time of the access token, and
     * by checking if the access token is null or empty.
     * **Here we are not calling the introspection endpoint to validate the access token!**
     *
     * @param context Context of the application
     *
     * @return `true` if the access token is valid, `false` otherwise.
     */
    suspend fun validateAccessToken(context: Context): Boolean?

    /**
     * Logout the user from the application.
     *
     * @param clientId Client id of the application
     * @param idToken Id token of the user
     *
     * @throws [AuthnManagerException] If the logout fails
     * @throws [IOException] If the request fails due to a network error
     */
    suspend fun logout(clientId: String, idToken: String): Unit?
}
