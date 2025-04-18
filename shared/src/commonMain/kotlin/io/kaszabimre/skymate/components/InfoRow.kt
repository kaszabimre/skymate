package io.kaszabimre.skymate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun InfoRow(label: String? = null, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        label?.let {
            Text(text = "$label:", style = MaterialTheme.typography.bodySmall)
        }
        Text(text = value, style = MaterialTheme.typography.bodySmall)
    }
}
