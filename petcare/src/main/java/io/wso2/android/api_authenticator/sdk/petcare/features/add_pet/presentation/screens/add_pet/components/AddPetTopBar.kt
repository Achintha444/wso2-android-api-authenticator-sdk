package io.wso2.android.api_authenticator.sdk.petcare.features.add_pet.presentation.screens.add_pet.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.wso2.android.api_authenticator.sdk.petcare.R
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.UiUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetTopBar(
    navigateToHome: () -> Unit
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.home_logo),
                modifier = Modifier
                    .size(UiUtil.getScreenHeight().dp / 6)
                    .offset(x = (-16).dp)
                    .clickable { navigateToHome() },
                contentDescription = "Home Logo",
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(UiUtil.getScreenHeight().dp / 25)
                    .offset(x = 4.dp)
                    .clickable { },
                tint = MaterialTheme.colorScheme.tertiary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
    )
}
