package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.add
import hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons.remove
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IntInput(
    value: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onMinus: () -> Unit = { onValueChange((value - 1).toString()) },
    onPlus: () -> Unit = { onValueChange((value + 1).toString()) },
    minusEnabled: Boolean = true,
    plusEnabled: Boolean = true,
) {
    val textFieldState = rememberTextFieldState(initialText = value.toString())

    LaunchedEffect(value) {
        if (textFieldState.text.toString() != value.toString()) {
            textFieldState.setTextAndPlaceCursorAtEnd(value.toString())
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }
            .collectLatest { onValueChange(it.toString()) }
    }

    Card {
        Row(
            modifier = modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onMinus,
                enabled = minusEnabled,
            ) {
                Icon(
                    remove,
                    contentDescription = null,
                )
            }
            TextField(
                state = textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.size(60.dp, 30.dp),
                contentPadding = PaddingValues(8.dp, 2.dp),
            )
            IconButton(
                onClick = onPlus,
                enabled = plusEnabled,
            ) {
                Icon(
                    add,
                    contentDescription = null,
                )
            }
        }
    }
}
