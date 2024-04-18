package io.wso2.android.api_authenticator.sdk.provider.provider_managers.authenticate_handler

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import io.wso2.android.api_authenticator.sdk.models.auth_params.AuthParams
import io.wso2.android.api_authenticator.sdk.models.autheniticator.Authenticator
import io.wso2.android.api_authenticator.sdk.models.state.AuthenticationState

interface AuthenticateHandlerProviderManager {
    /**
     * Set the authenticators in this step of the authentication flow.
     *
     * @param authenticatorsInThisStep The list of authenticators in this step
     */
    fun setAuthenticatorsInThisStep(
        authenticatorsInThisStep: ArrayList<Authenticator>?
    )

    /**
     * Authenticate the user with the selected authenticator. This method is used to
     * get the full details of the selected authenticator, then perform the passed
     * authentication process.
     *
     * @param authenticatorId The authenticator ID string
     * @param authenticatorTypeString The authenticator type string
     * @param afterGetAuthenticator The function to be executed after getting the authenticator
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     *
     * TODO: This also need to be exposed from the Authentication Provider to support for authenticate with any authenticator
     */
    suspend fun authenticateWithAuthenticator(
        authenticatorId: String,
        authenticatorTypeString: String,
        afterGetAuthenticator: suspend (Authenticator) -> Unit
    ): Authenticator?

    /**
     * Common function in all authenticate methods
     *
     * @param context The context of the application
     * @param userSelectedAuthenticator The selected authenticator
     * @param authParams The authentication parameters of the selected authenticator
     * @param authParamsAsMap The authentication parameters of the selected authenticator as a LinkedHashMap<String, String>
     * with the key as the parameter name and the value as the parameter value
     *
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun commonAuthenticate(
        context: Context,
        userSelectedAuthenticator: Authenticator? = null,
        authParams: AuthParams? = null,
        authParamsAsMap: LinkedHashMap<String, String>? = null
    )

    /**
     * Redirect the user to the authenticator's authentication page.
     *
     * @param context The context of the application
     * @param authenticator The authenticator to redirect the user
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun redirectAuthenticate(
        context: Context,
        authenticator: Authenticator
    )

    /**
     * Authenticate the user with the Google authenticator.
     *
     * @param context The context of the application
     * @param nonce The nonce value to authenticate the user, which is sent by the Identity Server.
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun googleAuthenticate(context: Context, nonce: String)

    /**
     * Authenticate the user with the Google authenticator using the legacy one tap method.
     *
     * @param context The context of the application
     * @param googleAuthenticateResultLauncher The result launcher for the Google authentication process
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun googleLegacyAuthenticate(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    )

    /**
     * Handle the Google authentication result using the legacy one tap Google authentication.
     *
     * @param context The context of the application
     * @param resultCode The result code of the Google authentication process
     * @param data The [Intent] object that contains the result of the Google authentication process
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     */
    suspend fun handleGoogleNativeLegacyAuthenticateResult(
        context: Context,
        resultCode: Int,
        data: Intent
    )

    /**
     * Authenticate the user with the Passkey authenticator using Credential Manager API.
     *
     * @param context The context of the application
     * @param authenticator The authenticator selected to authenticate the user
     * @param allowCredentials The list of allowed credentials. Default is empty array.
     * @param timeout The timeout for the authentication. Default is 300000.
     * @param userVerification The user verification method. Default is "required"
     *
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     * emit: [AuthenticationState.Authenticated] - The user is authenticated to access the application
     * emit: [AuthenticationState.Unauthenticated] - The user is not authenticated to access the application
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    suspend fun passkeyAuthenticate(
        context: Context,
        authenticator: Authenticator,
        allowCredentials: List<String>? = null,
        timeout: Long? = null,
        userVerification: String? = null
    )
}
