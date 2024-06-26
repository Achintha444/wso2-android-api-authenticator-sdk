package io.wso2.android.api_authenticator.sdk.core_config.managers.discovery_manager.impl

import com.fasterxml.jackson.databind.JsonNode
import io.wso2.android.api_authenticator.sdk.core_config.managers.discovery_manager.DiscoveryManager
import io.wso2.android.api_authenticator.sdk.models.exceptions.AuthnManagerException
import io.wso2.android.api_authenticator.sdk.models.exceptions.DiscoveryManagerException
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
 * Implementation of [DiscoveryManager]
 *
 * @property client OkHttpClient instance to handle network calls
 * @property discoveryManagerImplRequestBuilder Request builder class to build the requests
 */
class DiscoveryManagerImpl private constructor(
    private val client: OkHttpClient,
    private val discoveryManagerImplRequestBuilder: DiscoveryManagerImplRequestBuilder
) : DiscoveryManager {

    companion object {
        /**
         * Instance of the [DiscoveryManagerImpl] that will be used throughout the application
         */
        private var discoveryManagerImplInstance: WeakReference<DiscoveryManagerImpl> =
            WeakReference(null)

        /**
         * Initialize the [DiscoveryManagerImpl] instance and return the instance.
         *
         * @param client OkHttpClient instance to handle network calls
         * @param discoveryManagerImplRequestBuilder Request builder class to build the requests
         *
         * @return Initialized [DiscoveryManagerImpl] instance
         */
        fun getInstance(
            client: OkHttpClient,
            discoveryManagerImplRequestBuilder: DiscoveryManagerImplRequestBuilder
        ): DiscoveryManagerImpl {
            var discoveryManagerImpl = discoveryManagerImplInstance.get()
            if (discoveryManagerImpl == null) {
                discoveryManagerImpl = DiscoveryManagerImpl(
                    client,
                    discoveryManagerImplRequestBuilder
                )
                discoveryManagerImplInstance = WeakReference(discoveryManagerImpl)
            }
            return discoveryManagerImpl
        }
    }

    /**
     * Call the discovery endpoint and return the response.
     *
     * @param discoveryEndpoint Discovery endpoint
     *
     * @return Discovery response as a [JsonNode]
     */
    override suspend fun callDiscoveryEndpoint(discoveryEndpoint: String): JsonNode =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val request: Request = discoveryManagerImplRequestBuilder.discoveryRequestBuilder(
                    discoveryEndpoint
                )

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    @Throws(IOException::class, AuthnManagerException::class)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            if (response.code == 200) {
                                continuation.resume(
                                    JsonUtil.getJsonObject(response.body!!.string())
                                )
                            } else {
                                continuation.resumeWithException(
                                    DiscoveryManagerException(
                                        if (response.message != "") response.message
                                        else DiscoveryManagerException.CANNOT_DISCOVER_ENDPOINTS
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
