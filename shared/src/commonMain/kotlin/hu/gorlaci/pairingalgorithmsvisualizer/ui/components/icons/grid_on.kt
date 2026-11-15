package hu.gorlaci.pairingalgorithmsvisualizer.ui.components.icons

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val grid_on: ImageVector
    get() {
        if (_grid_on != null) {
            return _grid_on!!
        }
        _grid_on =
            ImageVector.Builder(
                name = "grid_on",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(5f, 21f)
                        quadTo(4.18f, 21f, 3.59f, 20.41f)
                        reflectiveQuadTo(3f, 19f)
                        verticalLineTo(5f)
                        quadTo(3f, 4.17f, 3.59f, 3.59f)
                        reflectiveQuadTo(5f, 3f)
                        horizontalLineTo(19f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(21f, 5f)
                        verticalLineTo(19f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(19f, 21f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(5f, 19f)
                        horizontalLineTo(8.33f)
                        verticalLineTo(15.68f)
                        horizontalLineTo(5f)
                        verticalLineTo(19f)
                        close()
                        moveToRelative(5.33f, 0f)
                        horizontalLineToRelative(3.35f)
                        verticalLineTo(15.68f)
                        horizontalLineTo(10.33f)
                        verticalLineTo(19f)
                        close()
                        moveToRelative(5.35f, 0f)
                        horizontalLineTo(19f)
                        verticalLineTo(15.68f)
                        horizontalLineTo(15.68f)
                        verticalLineTo(19f)
                        close()
                        moveTo(5f, 13.68f)
                        horizontalLineTo(8.33f)
                        verticalLineTo(10.33f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(3.35f)
                        close()
                        moveToRelative(5.33f, 0f)
                        horizontalLineToRelative(3.35f)
                        verticalLineTo(10.33f)
                        horizontalLineTo(10.33f)
                        verticalLineToRelative(3.35f)
                        close()
                        moveToRelative(5.35f, 0f)
                        horizontalLineTo(19f)
                        verticalLineTo(10.33f)
                        horizontalLineTo(15.68f)
                        verticalLineToRelative(3.35f)
                        close()
                        moveTo(5f, 8.32f)
                        horizontalLineTo(8.33f)
                        verticalLineTo(5f)
                        horizontalLineTo(5f)
                        verticalLineTo(8.32f)
                        close()
                        moveToRelative(5.33f, 0f)
                        horizontalLineToRelative(3.35f)
                        verticalLineTo(5f)
                        horizontalLineTo(10.33f)
                        verticalLineTo(8.32f)
                        close()
                        moveToRelative(5.35f, 0f)
                        horizontalLineTo(19f)
                        verticalLineTo(5f)
                        horizontalLineTo(15.68f)
                        verticalLineTo(8.32f)
                        close()
                    }
                }
                .build()
        return _grid_on!!
    }

private var _grid_on: ImageVector? = null
