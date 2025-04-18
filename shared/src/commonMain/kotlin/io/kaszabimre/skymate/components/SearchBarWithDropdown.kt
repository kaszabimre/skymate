package io.kaszabimre.skymate.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kaszabimre.skymate.model.City
import org.jetbrains.compose.resources.stringResource
import skymate.shared.generated.resources.Res
import skymate.shared.generated.resources.search_city

@Composable
fun SearchBarWithDropdown(
    query: String,
    onQueryChanged: (String) -> Unit,
    cities: List<City>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCitySelected: (City) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                onQueryChanged(it)
                onExpandedChange(true)
            },
            label = { Text(stringResource(Res.string.search_city)) },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (expanded && cities.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text("${city.name}, ${city.country}") },
                        onClick = {
                            onCitySelected(city)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
