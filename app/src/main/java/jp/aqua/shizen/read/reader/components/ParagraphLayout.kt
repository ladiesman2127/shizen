package jp.aqua.shizen.read.reader.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign


// TODO Refactor and Add Indention
@Composable
fun ParagraphLayout(
    modifier: Modifier = Modifier,
    align: TextAlign? = TextAlign.Justify,
    indent: Int,
    density: Float,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val minHeight = placeables.minOf { it.height }
        val lineHeight = (minHeight * ReaderConstants.lineHeight).toInt()
        val wordGap = ReaderConstants.wordGap

        val rows = mutableListOf<List<Placeable>>()
        val rowGaps = mutableListOf<Int>()
        var row = mutableListOf<Placeable>()
        var rowWidth = indent


        placeables.forEachIndexed { index, placeable ->
            val additionalWidth = placeable.width + wordGap
            if (rowWidth + additionalWidth <= constraints.maxWidth) {
                row.add(placeable)
                rowWidth += additionalWidth
                if (index == placeables.lastIndex) {
                    val remainingSpace = constraints.maxWidth - rowWidth
                    val numberOfElements = row.size
                    rows.add(row)
                    rowGaps.add(
                        if(align == TextAlign.Center || align == TextAlign.Right)
                            getElementGap(
                            remainingSpace,
                            numberOfElements,
                            align
                            )
                        else
                            0
                    )
                }
            } else {
                rows.add(row)

                val remainingSpace = constraints.maxWidth - rowWidth
                val numberOfElements = row.size

                rowGaps.add(
                    getElementGap(remainingSpace, numberOfElements, align!!)
                )

                row = mutableListOf(placeable)
                rowWidth = placeable.width

                if (index == placeables.lastIndex) {
                    rows.add(row)
                    rowGaps.add(0)
                }
            }
        }

        layout(
            width = constraints.maxWidth,
            height = (rows.size) * lineHeight
        ) {
            var xPos = indent
            var yPos = 0
            rows.forEachIndexed { index, row ->
                val gap = rowGaps[index]
                if (align == TextAlign.Right || align == TextAlign.Center)
                    xPos += gap
                row.forEach { placeable ->
                    placeable.place(
                        x = xPos,
                        y = yPos
                    )
                    xPos += placeable.width + wordGap
                    if (align == TextAlign.Justify)
                        xPos += gap
                }
                xPos = 0
                yPos += lineHeight
            }
        }
    }
}

private fun getElementGap(
    remainingSpace: Int,
    numberOfElements: Int,
    alignment: TextAlign
): Int {
    return when (alignment) {

        TextAlign.Left -> {
            0
        }

        TextAlign.Right -> {
            remainingSpace
        }

        TextAlign.Center -> {
            remainingSpace / 2
        }

        else -> {
            if (numberOfElements > 2)
                remainingSpace / (numberOfElements - 1)
            else
                remainingSpace
        }

    }
}



