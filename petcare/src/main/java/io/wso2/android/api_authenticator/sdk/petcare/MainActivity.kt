package io.wso2.android.api_authenticator.sdk.petcare

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.wso2.android.api_authenticator.sdk.petcare.ui.theme.Api_authenticator_sdkTheme
import io.wso2.android.api_authenticator.sdk.petcare.util.Event
import io.wso2.android.api_authenticator.sdk.petcare.util.EventBus
import io.wso2.android.api_authenticator.sdk.petcare.util.navigation.NavDestination
import io.wso2.android.api_authenticator.sdk.petcare.util.navigation.NavGraph
import io.wso2.android.api_authenticator.sdk.petcare.util.navigation.NavigationViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Api_authenticator_sdkTheme {
                val lifecycle = LocalLifecycleOwner.current.lifecycle
                val navigationController = rememberNavController()

                LaunchedEffect(key1 = lifecycle) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        EventBus.events.collect { event ->
                            when (event) {
                                is Event.Toast -> {
                                    // Show toast
                                    Toast.makeText(
                                        this@MainActivity,
                                        event.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    NavigationViewModel.navigationEvents.collect {
                        when (it) {
                            is NavigationViewModel.Companion.NavigationEvent.NavigateBack -> {
                                navigationController.popBackStack()
                            }

                            is NavigationViewModel.Companion.NavigationEvent.NavigateToLanding -> {
                                navigationController.navigate(NavDestination.LandingScreen)
                            }

                            is NavigationViewModel.Companion.NavigationEvent.NavigateToAuthWithData -> {
                                navigationController.navigate(
                                    "${NavDestination.AuthScreen}?authenticationFlow={authenticationFlow}"
                                        .replace(
                                            "{authenticationFlow}",
                                            newValue = it.data
                                        )
                                )
                            }

                            is NavigationViewModel.Companion.NavigationEvent.NavigateToHome -> {
                                navigationController.navigate(NavDestination.HomeScreen)
                            }

                            is NavigationViewModel.Companion.NavigationEvent.NavigateToProfile -> {
                                navigationController.navigate(NavDestination.ProfileScreen)
                            }
                        }
                    }
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NavGraph(navController = navigationController)
                    //HomeScreen()
                }
            }
        }
    }
}
