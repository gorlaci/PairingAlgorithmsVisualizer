package hu.gorlaci.pairingalgorithmsvisualizer.util

import kotlinx.browser.window

actual fun isTouchDevice(): Boolean = window.matchMedia("(pointer: coarse)").matches
