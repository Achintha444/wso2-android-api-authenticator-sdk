package io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.wso2.android.api_authenticator.sdk.petcare.features.home.domain.repository.PetRepository
import io.wso2.android.api_authenticator.sdk.petcare.features.login.domain.repository.AsgardeoAuthRepository
import io.wso2.android.api_authenticator.sdk.petcare.util.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    asgardeoAuthRepository: AsgardeoAuthRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    companion object {
        const val TAG = "HomeScreen"
    }

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state

    private val authenticationProvider = asgardeoAuthRepository.getAuthenticationProvider()

    init {
        getPets()
    }

    private fun getPets() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                val pets = petRepository.getPets()
                _state.update {
                    it.copy(
                        pets = pets
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message!!
                    )
                }
            }
        }
    }

    fun navigateToProfile() {
        viewModelScope.launch {
            NavigationViewModel.navigationEvents.emit(
                NavigationViewModel.Companion.NavigationEvent.NavigateToProfile
            )
        }
    }
}
