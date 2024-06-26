package io.wso2.android.api_authenticator.sdk.petcare.features.add_pet.presentation.screens.add_pet

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.wso2.android.api_authenticator.sdk.petcare.R
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.TopBar
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.profile.NameSection
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.profile.ProfileAttribute
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.profile.ProfileImage
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.profile.SettingsAttribute
import io.wso2.android.api_authenticator.sdk.petcare.ui.theme.Api_authenticator_sdkTheme
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.LoadingDialog

@Composable
internal fun ProfileScreen(
    viewModel: AddPetScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val navigateToHome: () -> Unit = viewModel::navigateToHome

    LoadingDialog(isLoading = state.value.isLoading)
    AddPetScreenContent(state.value, navigateToHome)
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AddPetScreenContent(
    state: AddPetScreenState,
    navigateToHome: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopBar(navigateToHome = navigateToHome, navigateToProfile = {})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AddPetScreenPreview() {
    Api_authenticator_sdkTheme {
        AddPetScreenContent(
            state = AddPetScreenState(
                isLoading = false
            ),
            navigateToHome = {}
        )
    }
}