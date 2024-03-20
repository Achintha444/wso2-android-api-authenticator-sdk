package io.wso2.android.api_authenticator.sdk.sample.presentation.screens.auth_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.wso2.android.api_authenticator.sdk.models.auth_params.AuthParams
import io.wso2.android.api_authenticator.sdk.models.autheniticator_type.AuthenticatorType
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlow
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlowNotSuccess
import io.wso2.android.api_authenticator.sdk.models.flow_status.FlowStatus
import io.wso2.android.api_authenticator.sdk.models.state.AuthenticationState
import io.wso2.android.api_authenticator.sdk.sample.domain.repository.AuthenticationRepository
import io.wso2.android.api_authenticator.sdk.sample.domain.repository.ProviderRepository
import io.wso2.android.api_authenticator.sdk.sample.presentation.util.sendEvent
import io.wso2.android.api_authenticator.sdk.sample.util.Event
import io.wso2.android.api_authenticator.sdk.sample.util.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val authenticationRepository: AuthenticationRepository,
    providerRepository: ProviderRepository
) : ViewModel() {

    companion object {
        const val TAG = "AuthScreen"
    }

    private val _state = MutableStateFlow(AuthScreenState())
    val state = _state

    private val authenticationProvider = providerRepository.getAuthenticationProvider()

    fun setAuthenticationFlow(authenticationFlow: AuthenticationFlow) {
        _state.update {
            it.copy(
                authenticationFlow = authenticationFlow as AuthenticationFlowNotSuccess,
                isLoading = false
            )
        }
    }

    fun authenticate(
        authenticatorType: AuthenticatorType,
        authenticatorParameters: AuthParams
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            authenticationRepository.authenticate(
                authenticatorType,
                authenticatorParameters
            )
                .onRight { authenticationFlow ->
                    // TODO: Move this to MutableSharedFlow
                    // Handle success
                    if (authenticationFlow.flowStatus == FlowStatus.SUCCESS.flowStatus) {
                        sendEvent(Event.Toast("Logged in successfully"))
                        NavigationViewModel.navigationEvents.emit(
                            NavigationViewModel.Companion.NavigationEvent.NavigateToHome
                        )
                    } else {
                        setAuthenticationFlow(authenticationFlow)
                    }
                }
                .onLeft { authenticationError ->
                    // Handle error
                    _state.update {
                        it.copy(error = authenticationError.errorMessage)
                    }
                    sendEvent(Event.Toast(authenticationError.errorMessage))
                }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun authenticateWithUsernamePassword(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            authenticationProvider.authenticateWithUsernameAndPassword(
                applicationContext,
                username,
                password
            )
        }
    }

    fun authenticateWithTotp(
        token: String
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            authenticationProvider.authenticateWithTotp(
                applicationContext,
                token
            )
        }
    }
}
