package hu.gorlaci.pairingalgorithmsvisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun StepSelector(
    value: Int,
    maxValue: Int,
    onValueChange: (String) -> Unit,
    onPrevious: () -> Unit = { onValueChange((value - 1).toString()) },
    onNext: () -> Unit = { onValueChange((value + 1).toString()) },
    previousEnabled: Boolean = true,
    nextEnabled: Boolean = true,
    modifier: Modifier = Modifier,
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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onPrevious,
            enabled = previousEnabled,
        ) {
            Text("Vissza")
        }

        Card(
            modifier = Modifier.padding(10.dp, 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    state = textFieldState,
                    modifier = Modifier.width(50.dp).height(40.dp),
                    contentPadding = PaddingValues(8.dp, 0.dp),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                Text(
                    text = " / $maxValue",
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp),
                )
            }
        }

        Button(
            onClick = onNext,
            enabled = nextEnabled,
        ) {
            Text("Következő")
        }
    }
}

@Preview
@Composable
fun StepSelectorPreview() {


    StepSelector(
        value = 125,
        maxValue = 125,
        onValueChange = { },
    )
}