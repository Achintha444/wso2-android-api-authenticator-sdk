package io.wso2.android.api_authenticator.sdk.core.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession

/**
 * Class to handle the Chrome Custom Tab browser to open the redirect URL in redirection based authentication
 *
 * TODO: As a future improvement, we can consider giving the user the ability to change the theme of the Chrome Custom Tab
 */
internal object ChromeCustomTabBrowser {
    /**
     * The package name of the Chrome browser
     */
    private const val CHROME_PACKAGE_NAME = "com.android.chrome"

    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var customTabClient: CustomTabsClient? = null
    private var customTabsSession: CustomTabsSession? = null

    /**
     * Set the CustomTabsServiceConnection to bind the CustomTabsService
     *
     * @param context the context of the application [Context]
     */
    private fun setCustomTabsServiceConnection(context: Context) {
        customTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient
            ) {
                customTabClient = client
                customTabClient?.warmup(0)
                customTabsSession = customTabClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                customTabClient = null
                customTabsSession = null
            }
        }

        // Bind the CustomTabsService
        CustomTabsClient.bindCustomTabsService(
            context,
            CHROME_PACKAGE_NAME,
            customTabsServiceConnection as CustomTabsServiceConnection
        )
    }

    /**
     * Open the redirect URL in a Chrome Custom Tab
     *
     * @param context the context of the application [Context]
     * @param redirectUri the redirect URI to open [String]
     */
    internal fun openRedirectUrl(context: Context, redirectUri: String) {
        setCustomTabsServiceConnection(context)

        val uri = Uri.parse(redirectUri)

        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(true)
            .build()
        customTabsIntent.intent.setPackage(CHROME_PACKAGE_NAME)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        customTabsIntent.launchUrl(context, uri)
    }
}
