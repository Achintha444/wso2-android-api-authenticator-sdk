package io.wso2.android.api_authenticator.sdk.models.exceptions

import io.wso2.android.api_authenticator.sdk.core.managers.app_auth.AppAuthManager

/**
 * Exception to be thrown to the exception related to the [AppAuthManager]
 *
 * @property message Message related to the exception
 * @property exceptionMessage Message related to the exception
 *
 * TODO: Make a mapper function to map the exceptions to the error types, and these need to be SEALED classes
 */
class AppAuthManagerException(
    override val message: String?,
    private val exceptionMessage: String? = null
): Exception(message) {
    companion object {
        /**
         * Authenticator exception TAG
         */
        const val APP_AUTH_MANAGER_EXCEPTION = "App Auth Manager Exception"

        /**
         * Message to be shown when authenticator is not initialized
         */
        const val TOKEN_REQUEST_FAILED = "Token request failed"

        /**
         * Message to be shown when token response is empty
         */
        const val EMPTY_TOKEN_RESPONSE = "Token response is empty"

        /**
         * Message to be shown when token response is empty or invalid
         */
        const val INVALID_REFRESH_TOKEN = "Invalid refresh token or refresh token is expired"

        /**
         * Message to be shown when Auth state is invalid
         */
        const val INVALID_AUTH_STATE = "Invalid authentication state"
    }

    override fun toString(): String {
        return "$APP_AUTH_MANAGER_EXCEPTION: $message $exceptionMessage"
    }

    /**
     * Print the exception
     */
    fun printException() {
        println(toString())
    }
}
