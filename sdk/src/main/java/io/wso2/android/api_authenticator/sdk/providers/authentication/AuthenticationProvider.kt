package io.wso2.android.api_authenticator.sdk.providers.authentication

import android.content.Context
import io.wso2.android.api_authenticator.sdk.core.AuthenticationCoreConfig
import io.wso2.android.api_authenticator.sdk.core.AuthenticationCoreDef
import io.wso2.android.api_authenticator.sdk.core.managers.authenticator.AuthenticatorManager
import io.wso2.android.api_authenticator.sdk.models.auth_params.AuthParams
import io.wso2.android.api_authenticator.sdk.models.auth_params.BasicAuthenticatorAuthParams
import io.wso2.android.api_authenticator.sdk.models.auth_params.TotpAuthenticatorTypeAuthParams
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.AuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.BasicAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.TotpAuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlow
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlowNotSuccess
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlowSuccess
import io.wso2.android.api_authenticator.sdk.models.exceptions.AuthenticatorTypeException
import io.wso2.android.api_authenticator.sdk.models.flow_status.FlowStatus
import io.wso2.android.api_authenticator.sdk.providers.di.AuthenticationProviderContainer
import io.wso2.android.api_authenticator.sdk.providers.util.AuthenticatorProviderUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference

/**
 * Authentication provider class that is used to manage the authentication process.
 *
 * @property authenticationCoreConfig [AuthenticationCoreConfig] object
 */
class AuthenticationProvider private constructor(
    private val authenticationCoreConfig: AuthenticationCoreConfig
) {
    /**
     * Instance of the [AuthenticationCoreDef] that will be used throughout the application
     */
    private var authenticationCore: AuthenticationCoreDef =
        AuthenticationProviderContainer.getAuthenticationCoreDef(
            authenticationCoreConfig
        )

    private val _authStateFlow = MutableStateFlow<AuthenticationState>(AuthenticationState.Initial)

    /**
     * Flow of the authentication state which is exposed to the outside.
     */
    val authenticationStateFlow: SharedFlow<AuthenticationState> = _authStateFlow.asSharedFlow()

    /**
     * List of authenticators in the current step of the authentication flow
     */
    private var authenticatorsInThisStep: ArrayList<AuthenticatorType>? = null

    companion object {
        /**
         * Instance of the [AuthenticationProvider] that will be used throughout the application
         */
        private var authenticationProviderInstance: WeakReference<AuthenticationProvider> =
            WeakReference(null)

        /**
         * Initialize the [AuthenticationProvider] instance and return the instance.
         *
         * @param authenticationCoreConfig The [AuthenticatorManager] instance
         */
        fun getInstance(
            authenticationCoreConfig: AuthenticationCoreConfig
        ): AuthenticationProvider {
            var authenticatorProvider = authenticationProviderInstance.get()
            if (authenticatorProvider == null) {
                authenticatorProvider = AuthenticationProvider(authenticationCoreConfig)
                authenticationProviderInstance = WeakReference(authenticatorProvider)
            }
            return authenticatorProvider
        }
    }

    /**
     * Initialize the authentication process.
     * This method will initialize the authentication process and emit the state of the authentication process.
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Unauthorized] - The user is not authorized to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun initializeAuthentication() {
        _authStateFlow.tryEmit(AuthenticationState.Loading)

        runBlocking {
            runCatching {
                authenticationCore.authorize()
            }.onSuccess {
                authenticatorsInThisStep =
                    (it as AuthenticationFlowNotSuccess)?.nextStep?.authenticators
                _authStateFlow.tryEmit(AuthenticationState.Unauthorized(it))
            }.onFailure {
                _authStateFlow.tryEmit(AuthenticationState.Error(it))
            }
        }
    }

    /**
     * Emit the success state based on the flow status of the [AuthenticationFlow]
     *
     * @param authenticationFlow [AuthenticationFlow] object
     * @param authStateFlow [MutableStateFlow] of [AuthenticationState]
     */
    private suspend fun emitSuccessStateOnFlowStatus(
        context: Context,
        authenticationFlow: AuthenticationFlow,
        authStateFlow: MutableStateFlow<AuthenticationState>
     ) {
        when (authenticationFlow.flowStatus) {
            FlowStatus.SUCCESS.flowStatus -> {
                // Clear the authenticators when the authentication is successful
                authenticatorsInThisStep = null

                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        authenticationCore.exchangeAuthorizationCode(
                            (authenticationFlow as AuthenticationFlowSuccess).authData.code,
                            context
                        )
                    }.onSuccess {
                        authStateFlow.tryEmit(
                            AuthenticationState.Authorized
                        )
                    }.onFailure {
                        authStateFlow.tryEmit(
                            AuthenticationState.Error(it)
                        )
                    }
                }

//                runBlocking {
//                    runCatching {
//                        authenticationCore.exchangeAuthorizationCode(
//                            (authenticationFlow as AuthenticationFlowSuccess).authData.code,
//                            context
//                        )
//                    }.onSuccess {
//                        authStateFlow.tryEmit(
//                            AuthenticationState.Authorized
//                        )
//                    }.onFailure {
//                        authStateFlow.tryEmit(
//                            AuthenticationState.Error(it)
//                        )
//                    }
//                }

//                try {
//                    val tokenResponse = authenticationCore.exchangeAuthorizationCode(
//                        (authenticationFlow as AuthenticationFlowSuccess).authData.code,
//                        context
//                    )
//                    authStateFlow.tryEmit(
//                        AuthenticationState.Authorized
//                    )
//                } catch (e: Exception) {
//                    authStateFlow.tryEmit(
//                        AuthenticationState.Error(e)
//                    )
//                }

//                authenticationCore.exchangeAuthorizationCode(
//                    (authenticationFlow as AuthenticationFlowSuccess).authData.code,
//                    context
//                )
            }

            else -> {
                // Update the authenticators for the next step when the authentication is not complete
                authenticatorsInThisStep =
                    (authenticationFlow as AuthenticationFlowNotSuccess)?.nextStep?.authenticators

                authStateFlow.tryEmit(
                    AuthenticationState.Unauthorized(
                        authenticationFlow
                    )
                )
            }
        }
    }

    /**
     * Handle the state when the authenticator type is not found
     *
     * @param authStateFlow [MutableStateFlow] of [AuthenticationState]
     * @param authenticators List of authenticators
     * @param authenticatorTypeString Authenticator type string
     *
     * @return Boolean value whether the authenticator type is not found
     * `true` if the authenticator type is not found, `false` otherwise
     */
    private fun handleStateWhenAuthenticatorTypeIsNotFound(
        authStateFlow: MutableStateFlow<AuthenticationState>,
        authenticators: ArrayList<AuthenticatorType>,
        authenticatorTypeString: String
    ): AuthenticatorType? {
        val authenticatorType: AuthenticatorType? =
            AuthenticatorProviderUtil.getAuthenticatorTypeFromAuthenticatorTypeList(
                authenticators,
                authenticatorTypeString
            )

        if (authenticatorType == null) {
            authStateFlow.tryEmit(
                AuthenticationState.Error(
                    AuthenticatorTypeException(
                        AuthenticatorTypeException.AUTHENTICATOR_NOT_FOUND_OR_MORE_THAN_ONE,
                        authenticatorTypeString
                    )
                )
            )
            return null
        } else {
            return authenticatorType
        }
    }

    /**
     * Common function in all authenticate methods
     *
     * @param authenticatorTypeString Authenticator type string
     * @param authParams [AuthParams] object
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authorized] - The user is authorized to access the application
     * emit: [AuthenticationState.Unauthorized] - The user is not authorized to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    private suspend fun commonAuthenticate(
        context: Context,
        authenticatorTypeString: String,
        authParams: AuthParams
    ) {
        _authStateFlow.tryEmit(AuthenticationState.Loading)

        val authenticatorType: AuthenticatorType? =
            handleStateWhenAuthenticatorTypeIsNotFound(
                _authStateFlow,
                authenticatorsInThisStep!!,
                authenticatorTypeString
            )

        if (authenticatorType != null) {
            runBlocking {
                runCatching {
                    authenticationCore.authenticate(
                        authenticatorType,
                        authParams
                    )
                }.onSuccess {
                    emitSuccessStateOnFlowStatus(context, it!!, _authStateFlow)
                }.onFailure {
                    _authStateFlow.tryEmit(AuthenticationState.Error(it))
                }
            }
        }
    }

    /**
     * Authenticate the user with the username and password.
     *
     * @param username The username of the user
     * @param password The password of the user
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authorized] - The user is authorized to access the application
     * emit: [AuthenticationState.Unauthorized] - The user is not authorized to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun authenticateWithUsernameAndPassword(
        context: Context,
        username: String,
        password: String
    ) {
        commonAuthenticate(
            context,
            BasicAuthenticatorType.AUTHENTICATOR_TYPE,
            BasicAuthenticatorAuthParams(username, password)
        )
    }

    /**
     * Authenticate the user with the TOTP token.
     *
     * @param token The TOTP token of the user
     *
     * emit: [AuthenticationState.Loading] - The application is in the process of loading the authentication state
     * emit: [AuthenticationState.Authorized] - The user is authorized to access the application
     * emit: [AuthenticationState.Unauthorized] - The user is not authorized to access the application
     * emit: [AuthenticationState.Error] - An error occurred during the authentication process
     */
    suspend fun authenticateWithTotp(
        context: Context,
        token: String
    ) {
        commonAuthenticate(
            context,
            TotpAuthenticatorType.AUTHENTICATOR_TYPE,
            TotpAuthenticatorTypeAuthParams(token)
        )
    }
}