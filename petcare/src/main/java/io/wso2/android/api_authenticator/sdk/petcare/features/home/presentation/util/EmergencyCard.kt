package io.wso2.android.api_authenticator.sdk.petcare.features.home.presentation.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.wso2.android.api_authenticator.sdk.petcare.util.ui.UiUtil

@Composable
fun EmergencyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.error)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp, 24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),

                ) {
                Text(
                    text = "Need emergency medical help?",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Our veterinarian’s are ready to help you",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Icon(
                imageVector = Icons.Outlined.Call,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(UiUtil.getScreenHeight().dp / 25)
            )
        }

    }
}
