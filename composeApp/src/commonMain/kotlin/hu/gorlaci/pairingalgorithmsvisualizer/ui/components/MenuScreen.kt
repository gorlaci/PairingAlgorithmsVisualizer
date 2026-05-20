package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MenuItem(val text: String, val onClick: () -> Unit)

@Composable
fun MenuScreen(
    title: String,
    items: List<MenuItem>,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTopAppbar(
                title = title,
                onBack = onBack,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(paddingValues),
        ) {
            if (items.isNotEmpty()) {
                items.dropLast(1).forEach { item ->
                    Button(onClick = item.onClick) {
                        Text(item.text)
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }
                Button(onClick = items.last().onClick) {
                    Text(items.last().text)
                }
            }
        }
    }
}
