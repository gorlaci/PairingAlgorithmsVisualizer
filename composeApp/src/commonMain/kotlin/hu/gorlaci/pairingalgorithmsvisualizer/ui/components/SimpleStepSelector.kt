package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import pairingalgorithmsvisualizer.composeapp.generated.resources.Res
import pairingalgorithmsvisualizer.composeapp.generated.resources.back_button
import pairingalgorithmsvisualizer.composeapp.generated.resources.next_button

@Composable
fun SimpleStepSelector(
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    nextEnabled: Boolean = true,
    backEnabled: Boolean = true,
) {
    Row(modifier = modifier) {
        Button(
            onClick = onBack,
            enabled = backEnabled,
        ) {
            Text(stringResource(Res.string.back_button))
        }
        Button(
            onClick = onNext,
            enabled = nextEnabled,
        ) {
            Text(stringResource(Res.string.next_button))
        }
    }
}
