package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.arrow_back

@Composable
fun SimpleTopAppbar(
    title: String,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(arrow_back, contentDescription = "Back")
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            ),
    )
}
