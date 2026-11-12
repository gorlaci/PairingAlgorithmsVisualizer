package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CrossableText(
    text: String,
    crossed: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.wrapContentSize()) {
        Text(text = text, modifier = modifier)

        if (crossed) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawLine(
                    color = androidx.compose.ui.graphics.Color.Black,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = 2f,
                )
                drawLine(
                    color = androidx.compose.ui.graphics.Color.Black,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = 2f,
                )
            }
        }
    }
}

@Preview
@Composable
fun CrossableTextPreview() {
    CrossableText(text = "A", crossed = true)
}
