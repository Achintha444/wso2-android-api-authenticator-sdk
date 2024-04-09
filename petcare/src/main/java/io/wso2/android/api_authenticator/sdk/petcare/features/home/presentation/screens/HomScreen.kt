package io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.wso2.android.api_authenticator.sdk.petcare.R
import io.wso2.android.api_authenticator.sdk.petcare.features.home.domain.models.UserDetails
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.AddPetFab
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.DoctorSearchField
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.EmergencyCard
import io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util.VetCard
import io.wso2.android.api_authenticator.sdk.petcare.ui.theme.Api_authenticator_sdkTheme
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.UiUtil

@Composable
internal fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContent(state.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreenContent(
    state: HomeScreenState
) {
    val languages = listOf(
        "C++", "C", "C#", "Java", "Kotlin", "Dart", "Python", "Javascript", "SpringBoot",
        "XML", "Dart", "Node JS", "Typescript", "Dot Net", "GoLang", "MongoDb",
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.home_logo),
                        modifier = Modifier
                            .size(UiUtil.getScreenHeight().dp / 6)
                            .offset(x = (-16).dp),
                        contentDescription = "Home Logo",
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(UiUtil.getScreenHeight().dp / 25)
                            .offset(x = 4.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            )
        },
        floatingActionButton = { AddPetFab() },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DoctorSearchField()
                VetCard()
                EmergencyCard()
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                thickness = 0.5.dp
            )
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
            ) {
                Text(
                    text = "Your cutie pets",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Column() {
                    // on below line we are populating
                    // items for listview.
                    languages.forEach { language ->
                        // on below line we are specifying ui for each item of list view.
                        // we are specifying a simple text for each item of our list view.
                        Text(text = language, modifier = Modifier.padding(15.dp))
                        // on below line we are specifying
                        // divider for each list item
                        Divider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    Api_authenticator_sdkTheme {
        HomeScreenContent(
            HomeScreenState(
                isLoading = false,
                user = UserDetails(
                    imageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=3570&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    username = "JohnDoe",
                    email = "john@wso2.com",
                    firstName = "John",
                    lastName = "Doe"
                )
            )
        )
    }
}
