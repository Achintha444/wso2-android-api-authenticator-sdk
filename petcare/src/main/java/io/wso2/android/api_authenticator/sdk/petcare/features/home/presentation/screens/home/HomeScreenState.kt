package io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.screens.home

import io.wso2.android.api_authenticator.sdk.petcare.features.home.domain.models.Pet

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val pets: List<Pet> = emptyList()
)
