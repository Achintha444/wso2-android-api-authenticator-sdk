package io.wso2.android.api_authenticator.sdk.sample.presentation.screens.auth_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.wso2.android.api_authenticator.sdk.models.autheniticator.Authenticator
import io.wso2.android.api_authenticator.sdk.sample.R
import io.wso2.android.api_authenticator.sdk.sample.presentation.screens.auth_screen.AuthScreenViewModel

@Composable
internal fun GithubNativeAuth(
    viewModel: AuthScreenViewModel = hiltViewModel(),
    authenticatorType: Authenticator
) {
    GithubNativeAuthComponent(
        onSubmit = {
            viewModel.authenticateWithGithub(
                authenticatorType.authenticatorId
            )
        }
    )
}

@Composable
fun GithubNativeAuthComponent(
    onSubmit: () -> Unit
) {
    Button(
        onClick = onSubmit,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = stringResource(id = R.string.screens_auth_screen_github_login))
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GithubNativeAuthPreview() {
    GithubNativeAuthComponent(onSubmit = {})
}
