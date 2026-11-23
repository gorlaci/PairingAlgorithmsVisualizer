package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.util.isTouchDevice

data class MainMenuItem(
    val algorithmName: String,
    val algorithmImage: ImageBitmap,
    val onRunAlgorithm: () -> Unit,
    val onQuiz: (() -> Unit)? = null,
)

@Composable
fun MainMenuAlgorithmCard(
    menuItem: MainMenuItem,
    modifier: Modifier = Modifier,
) {
    var hovered by remember { mutableStateOf(false) }

    val touchDevice = isTouchDevice()

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> {
                                hovered = true
                            }

                            PointerEventType.Exit -> {
                                hovered = false
                            }
                        }
                    }
                }
            },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                bitmap = menuItem.algorithmImage,
                contentDescription = menuItem.algorithmName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
            ) {
                AnimatedVisibility(
                    visible = touchDevice || hovered,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
                    ) {
                        Button(onClick = menuItem.onRunAlgorithm) {
                            Text(text = "Algoritmus futtatása")
                        }

                        if (menuItem.onQuiz != null) {
                            Button(onClick = menuItem.onQuiz) {
                                Text(text = "Kvíz")
                            }
                        }
                    }
                }
            }
            Text(
                text = menuItem.algorithmName,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}
