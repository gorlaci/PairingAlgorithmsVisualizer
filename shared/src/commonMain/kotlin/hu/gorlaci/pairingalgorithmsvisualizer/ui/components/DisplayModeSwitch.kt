package hu.gorlaci.pairingalgorithmsvisualizer.ui.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.enums.enumEntries

@Composable
inline fun <reified DisplayModeType : Enum<DisplayModeType>> DisplayModeSwitch(
    selected: DisplayModeType,
    crossinline onDisplayModeSelected: (DisplayModeType) -> Unit,
    crossinline label: (DisplayModeType) -> (@Composable () -> Unit),
    modifier: Modifier = Modifier,
) {
    val displayModeEntries = enumEntries<DisplayModeType>()

    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        displayModeEntries.forEachIndexed { index, displayModeEntry ->
            SegmentedButton(
                shape = androidx.compose.material3.SegmentedButtonDefaults.itemShape(
                    index,
                    displayModeEntries.size,
                ),
                selected = selected == displayModeEntry,
                onClick = { onDisplayModeSelected(displayModeEntry) },
                label = label(displayModeEntry),
                icon = {},
            )
        }
    }
}
