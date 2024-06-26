package io.wso2.android.api_authenticator.sdk.core.managers.logout.impl

import io.wso2.android.api_authenticator.sdk.core_config.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core.managers.authn.impl.AuthnManagerImpl
import io.wso2.android.api_authenticator.sdk.core.managers.logout.LogoutManager
import io.wso2.android.api_authenticator.sdk.models.exceptions.LogoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Implementation of [LogoutManager]
 * This manager is responsible for handling the logout of the user from the application
 *
 * @property authenticationCoreConfig [AuthenticationCoreConfig] to get the logout URL
 * @property client [OkHttpClient] to make the logout request
 * @property logoutRequestBuilder [LogoutManagerImplRequestBuilder] to build the logout request
 */
class LogoutManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val client: OkHttpClient,
    private val logoutRequestBuilder: LogoutManagerImplRequestBuilder
) : LogoutManager {
    companion object {
        /**
         * Instance of the [LogoutManagerImpl] that will be used throughout the application
         */
        private var logoutManagerImplInstance: WeakReference<LogoutManagerImpl>
            = WeakReference(null)

        /**
         * Initialize the [LogoutManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         * @param client OkHttpClient instance to handle network calls
         * @param logoutRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [AuthnManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            client: OkHttpClient,
            logoutRequestBuilder: LogoutManagerImplRequestBuilder
        ): LogoutManagerImpl {
            var logoutManagerImpl = logoutManagerImplInstance.get()
            if (logoutManagerImpl == null) {
                logoutManagerImpl = LogoutManagerImpl(
                    authenticationCoreConfig,
                    client,
                    logoutRequestBuilder
                )
                logoutManagerImplInstance = WeakReference(logoutManagerImpl)
            }
            return logoutManagerImpl
        }
    }

    /**
     * Logout the user from the application.
     *
     * @param idToken Id token of the user
     *
     * @throws [LogoutException] If the logout fails
     * @throws [IOException] If the request fails due to a network error
     */
    override suspend fun logout(idToken: String): Unit? =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request: Request = logoutRequestBuilder.logoutRequestBuilder(
                    authenticationCoreConfig.getLogoutUrl(),
                    authenticationCoreConfig.getClientId(),
                    idToken
                )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code != 200) {
                                // Throw an [LogoutException] if the request does not return 200 response.message
                                continuation.resumeWithException(LogoutException(response.message))
                            } else {
                                continuation.resume(Unit)
                            }
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                })
            }
        }
}
