package io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.wso2.android.api_authenticator.sdk.models.authentication_flow.AuthenticationFlow
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.AuthUI
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.BasicAuthComponent
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.GetStarted
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.GithubAuthComponent
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.GoogleNativeAuthComponent
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.util.common_component.ContinueText
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.LoadingDialog
import io.wso2.android.api_authenticator.sdk.petcare.ui.theme.Api_authenticator_sdkTheme
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components.PasskeyAuthComponent

@Composable
internal fun AuthScreen(
    viewModel: AuthScreenViewModel = hiltViewModel(),
    authenticationFlow: AuthenticationFlow
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = authenticationFlow) {
        viewModel.setAuthenticationFlow(authenticationFlow)
    }
    LoadingDialog(isLoading = state.value.isLoading)
    AuthScreenContent(state.value)
}

@Composable
fun AuthScreenContent(state: AuthScreenState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
                .offset(y = 32.dp)
        ) {
            GetStarted()
        }
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 32.dp,
                        end = 32.dp,
                        bottom = 32.dp
                    )
                    .fillMaxSize()
            ) {
                state.authenticationFlow?.let { authenticationFlow ->
                    AuthUI(authenticationFlow)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AuthScreenPreview() {
    Api_authenticator_sdkTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
                    .offset(y = 42.dp)
            ) {
                GetStarted()
            }
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 32.dp,
                            end = 32.dp,
                            bottom = 32.dp
                        )
                        .fillMaxSize(1f)
                ) {
                    BasicAuthComponent(onLoginClick = { _, _ -> })
                    ContinueText()
                    GoogleNativeAuthComponent {}
                    PasskeyAuthComponent {}
                    GithubAuthComponent {}
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}