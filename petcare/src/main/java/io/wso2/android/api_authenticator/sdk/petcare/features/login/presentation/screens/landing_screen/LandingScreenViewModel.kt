package io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.landing_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.wso2.android.api_authenticator.sdk.models.state.AuthenticationState
import io.wso2.android.api_authenticator.sdk.petcare.features.login.domain.repository.AsgardeoAuthRepository
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.sendEvent
import io.wso2.android.api_authenticator.sdk.petcare.util.Event
import io.wso2.android.api_authenticator.sdk.sample.presentation.screens.landing_screen.LandingScreenState
import io.wso2.android.api_authenticator.sdk.petcare.util.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import javax.inject.Inject

@HiltViewModel
class LandingScreenViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    asgardeoAuthRepository: AsgardeoAuthRepository,
) : ViewModel() {
    companion object {
        const val TAG = "LandingScreen"
    }

    private val _state = MutableStateFlow(LandingScreenState())
    val state = _state

    private val authenticationProvider = asgardeoAuthRepository.getAuthenticationProvider()
    private val authenticationStateFlow = authenticationProvider.getAuthenticationStateFlow()

    init {
        handleAuthenticationState()
        isLoggedInStateFlow()
    }

    fun initializeAuthentication() {
        viewModelScope.launch {
            authenticationProvider.initializeAuthentication(applicationContext)
        }
    }

    private fun isLoggedInStateFlow() {
        viewModelScope.launch {
            authenticationProvider.isLoggedInStateFlow(applicationContext)
        }
    }

    private fun handleAuthenticationState() {
        viewModelScope.launch {
            authenticationStateFlow.collect {
                when (it) {
                    is AuthenticationState.Initial -> {
                        _state.update { landingScreenState ->
                            landingScreenState.copy(isLoading = false)
                        }
                    }

                    is AuthenticationState.Unauthenticated -> {
                        _state.update { landingScreenState ->
                            landingScreenState.copy(isLoading = false)
                        }
                        NavigationViewModel.navigationEvents.emit(
                            NavigationViewModel.Companion.NavigationEvent.NavigateToAuthWithData(
                                URLEncoder.encode(it.authenticationFlow!!.toJsonString(), "utf-8")
                            )
                        )
                    }

                    is AuthenticationState.Error -> {
                        _state.update { landingScreenState ->
                            landingScreenState.copy(error = it.toString(), isLoading = false)
                        }
                        sendEvent(Event.Toast(it.toString()))
                    }

                    is AuthenticationState.Authenticated -> {
                        _state.update { landingScreenState ->
                            landingScreenState.copy(isLoading = false)
                        }
                        NavigationViewModel.navigationEvents.emit(
                            NavigationViewModel.Companion.NavigationEvent.NavigateToHome
                        )
                    }

                    else -> {
                        _state.update { landingScreenState ->
                            landingScreenState.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}
