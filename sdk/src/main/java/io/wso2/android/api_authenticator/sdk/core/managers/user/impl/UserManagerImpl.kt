package io.wso2.android.api_authenticator.sdk.core.managers.user.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import io.wso2.android.api_authenticator.sdk.core_config.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core.managers.user.UserManager
import io.wso2.android.api_authenticator.sdk.models.exceptions.UserManagerException
import io.wso2.android.api_authenticator.sdk.util.JsonUtil
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
 * UserManagerImpl class is the implementation of the [UserManager] interface.
 * Which is used to handle the user details retrieval from the Identity Server.
 *
 * @property authenticationCoreConfig Configuration of the Identity Server [AuthenticationCoreConfig]
 * @property client OkHttpClient instance to handle network calls [OkHttpClient]
 * @property userManagerImplRequestBuilder Request builder class to build the requests [UserManagerImplRequestBuilder]
 */
class UserManagerImpl private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig,
    private val client: OkHttpClient,
    private val userManagerImplRequestBuilder: UserManagerImplRequestBuilder
) : UserManager {

    companion object {
        /**
         * Instance of the [UserManagerImpl] that will be used throughout the application
         */
        private var userManagerImplInstance: WeakReference<UserManagerImpl> = WeakReference(null)

        /**
         * Initialize the [UserManagerImpl] instance and return the instance.
         *
         * @param authenticationCoreConfig Configuration of the Authenticator [AuthenticationCoreConfig]
         * @param client OkHttpClient instance to handle network calls
         * @param userManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [UserManagerImpl] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig,
            client: OkHttpClient,
            userManagerImplRequestBuilder: UserManagerImplRequestBuilder,
        ): UserManagerImpl {
            var userManagerImpl = userManagerImplInstance.get()
            if (userManagerImpl == null) {
                userManagerImpl = UserManagerImpl(
                    authenticationCoreConfig,
                    client,
                    userManagerImplRequestBuilder,
                )
                userManagerImplInstance = WeakReference(userManagerImpl)
            }
            return userManagerImpl
        }
    }

    /**
     * Get the basic user information of the authenticated.
     *
     * @param accessToken Access token to authorize the request
     *
     * @return User details as a [LinkedHashMap]
     */
    override suspend fun getBasicUserInfo(accessToken: String?): LinkedHashMap<String, Any>? =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request: Request = userManagerImplRequestBuilder.getUserDetailsRequestBuilder(
                    authenticationCoreConfig.getUserinfoEndpoint(),
                    accessToken!!
                )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code == 200) {
                                // reading the json from the response
                                val responseObject: JsonNode =
                                    JsonUtil.getJsonObject(response.body!!.string())

                                val stepTypeReference =
                                    object : TypeReference<LinkedHashMap<String, Any>>() {}

                                continuation.resume(
                                    JsonUtil.jsonNodeToObject(responseObject, stepTypeReference)
                                )
                            } else {
                                continuation.resumeWithException(
                                    UserManagerException(
                                        UserManagerException.USER_MANAGER_EXCEPTION
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                })
            }
        }
}
