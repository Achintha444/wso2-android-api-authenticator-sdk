package io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.wso2.android.api_authenticator.sdk.models.autheniticator.Authenticator
import io.wso2.android.api_authenticator.sdk.petcare.R
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.screens.auth_screen.AuthScreenViewModel
import io.wso2.android.api_authenticator.sdk.petcare.features.login.presentation.util.common_component.AuthButton

@Composable
internal fun OpenIdRedirectAuth(
    viewModel: AuthScreenViewModel = hiltViewModel(),
    authenticator: Authenticator
) {
    OpenIdRedirectAuthComponent(
        onSubmit = {
            viewModel.authenticateWithOpenIdConnect(authenticator.authenticatorId)
        }
    )
}

@Composable
fun OpenIdRedirectAuthComponent(
    onSubmit: () -> Unit
) {
    AuthButton(
        onSubmit = onSubmit,
        label = "Continue with OpenID Connect",
        imageResource = R.drawable.enterprise_icon,
        imageContextDescription = "OpenID Connect"
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OpenIdRedirectAuthPreview() {
    OpenIdRedirectAuthComponent(onSubmit = {})
}
