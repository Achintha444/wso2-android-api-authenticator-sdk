package io.wso2.android.api_authenticator.sdk.core.managers.native_authentication_handler.google_native_legacy_authentication_handler

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher

/**
 * Interface to be implemented by the Google Native Authentication Handler Manager
 * This manager is responsible for handling the Google Native Authentication using the
 * legacy one tap method
 */
interface GoogleNativeLegacyAuthenticationHandlerManager {
    /**
     * Authenticate the user with Google using the legacy one tap method.
     *
     * @param context [Context] of the application
     * @param googleAuthenticateResultLauncher [ActivityResultLauncher] to launch the Google authentication intent
     */
    suspend fun authenticateWithGoogleNativeLegacy(
        context: Context,
        googleAuthenticateResultLauncher: ActivityResultLauncher<Intent>
    )

    /**
     * Handle the Google native authentication result.
     *
     * @param result The [ActivityResult] object that contains the result of the Google authentication process
     *
     * @return The Google native authenticator parameters [LinkedHashMap] that contains the ID Token and the Auth Code
     */
    suspend fun handleGoogleNativeLegacyAuthenticateResult(result: ActivityResult)
            : LinkedHashMap<String, String>?

    /**
     * Logout the user from the Google account
     *
     * @param context [Context] of the application
     */
    fun logout(context: Context)
}
