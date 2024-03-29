package io.wso2.android.api_authenticator.sdk.provider.providers.authentication.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.wso2.android.api_authenticator.sdk.models.state.AuthenticationState
import io.wso2.android.api_authenticator.sdk.provider.provider_managers.authentication.AuthenticationProviderManager
import io.wso2.android.api_authenticator.sdk.provider.providers.authentication.AuthenticationProvider
import kotlinx.coroutines.flow.SharedFlow
import java.lang.ref.WeakReference

/**
 * Authentication provider class that is used to manage the authentication process.
 *
 * @param authenticationProviderManager The [AuthenticationProviderManager] instance
 *
 * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
 * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
 * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
 * emit: [AuthenticationState.Error] - An error occurred during the authentication process
 */
internal class AuthenticationProviderImpl private constructor(
    private val authenticationProviderManager: AuthenticationProviderManager
) : AuthenticationProvider {

    companion object {
        /**
         * Instance of the [AuthenticationProviderImpl] that will be used throughout the application
         */
        private var authenticationProviderImplInstance: WeakReference<AuthenticationProviderImpl> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationProviderImpl] instance and return the instance.
         *
         * @param authenticationProviderManager The [AuthenticationProviderManager] instance
         *
         * @return The [AuthenticationProviderImpl] instance
         */
        fun getInstance(
            authenticationProviderManager: AuthenticationProviderManager,
        ): AuthenticationProviderImpl {
            var authenticatorProvider = authenticationProviderImplInstance.get()
            if (authenticatorProvider == null) {
                authenticatorProvider = AuthenticationProviderImpl(authenticationProviderManager)
                authenticationProviderImplInstance = WeakReference(authenticatorProvider)
            }
            return authenticatorProvider
        }

        /**
         * Get the [AuthenticationProviderImpl] instance.
         *
         * @return The [AuthenticationProviderImpl] instance
         */
        fun getInstance(): AuthenticationProviderImpl? = authenticationProviderImplInstance.get()
    }

    /**
     * Get authentication state flow
     *
     * @return authentication state flow [SharedFlow<AuthenticationState>]
     */
    override fun getAuthenticationStateFlow(): SharedFlow<AuthenticationState> =
        authenticationProviderManager.getAuthenticationStateFlow()

    /**
     * Check whether the user is logged in or not.
     *
     * @return `true` if the user is logged in, `false` otherwise
     */
    override suspend fun isLoggedIn(context: Context): Boolean =
        authenticationProviderManager.isLoggedIn(context)

    /**
     * Handle the authentication flow initially to check whether the user is authenticated or not.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Initial] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun isLoggedInStateFlow(context: Context) =
        authenticationProviderManager.isLoggedInStateFlow(context)

    /**
     * Initialize the authentication process.
     * This method will initialize the authentication process and emit the state of the authentication process.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun initializeAuthentication(context: Context) =
        authenticationProviderManager.initializeAuthentication(context)

    /**
     * Authenticate the user with the username and password.
     *
     * @param context The context of the application
     * @param username The username of the user
     * @param password The password of the user
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        username: String,
        password: String
    ) = authenticationProviderManager
        .authenticateWithUsernameAndPassword(context, username, password)

    /**
     * Authenticate the user with the TOTP token.
     *
     * @param context The context of the application
     * @param token The TOTP token of the user
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithTotp(context: Context, token: String) =
        authenticationProviderManager.authenticateWithTotp(context, token)

    /**
     * Authenticate the user with the selected authenticator which requires a redirect URI.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     */
    override suspend fun authenticateWithRedirectUri(
        context: Context,
        authenticatorId: String?,
        authenticatorType: String?
    ) = authenticationProviderManager
        .authenticateWithRedirectUri(context, authenticatorId, authenticatorType)

    /**
     * Authenticate the user with the OpenID Connect authenticator.
     *
     * @param context The context of the application
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithOpenIdConnect(context: Context) =
        authenticationProviderManager.authenticateWithOpenIdConnect(context)

    /**
     * Authenticate the user with the Github authenticator (Redirect).
     *
     * @param context The context of the application
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithGithubRedirect(context: Context) =
        authenticationProviderManager.authenticateWithGithubRedirect(context)

    /**
     * Authenticate the user with the Google authenticator.
     *
     * @param context The context of the application
     * @param googleAuthenticateResultLauncher The [ActivityResultLauncher] object to handle the Google authentication result
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override suspend fun authenticateWithGoogle(context: Context) = authenticationProviderManager
        .authenticateWithGoogle(context)

    /**
     * Authenticate the user with the Google authenticator using the legacy one tap method.
     *
     * @param context The context of the application
     * @param googleAuthenticateResultLauncher The result launcher for the Google authentication process
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithGoogleLegacy(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    ) = authenticationProviderManager
        .authenticateWithGoogleLegacy(context, googleAuthenticateResultLauncher)

    /**
     * Handle the Google authentication result.
     *
     * @param context The context of the application
     * @param result The [ActivityResult] object that contains the result of the Google authentication process
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     */
    override suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        result: ActivityResult
    ) = authenticationProviderManager.handleGoogleNativeLegacyAuthenticateResult(context, result)

    /**
     * Authenticate the user with the selected authenticator.
     *
     * @param context The context of the application
     * @param authenticatorId The authenticator id of the selected authenticator
     * @param authParams The authentication parameters of the selected authenticator
     * as a LinkedHashMap<String, String> with the key as the parameter name and the value as the
     * parameter value
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun authenticateWithAnyAuthenticator(
        context: Context,
        authenticatorId: String,
        authParams: LinkedHashMap<String, String>
    ) = authenticationProviderManager
        .authenticateWithAnyAuthenticator(context, authenticatorId, authParams)

    /**
     * Logout the user from the application.
     *
     * @param context The context of the application
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Initial] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    override suspend fun logout(context: Context) = authenticationProviderManager.logout(context)
}