package com.inlacou.lib.inkcomposeextensions.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inlacou.lib.inkcomposeextensions.Config
import isLandScape

@Composable
fun PanelSelector(
    modifier: Modifier = Modifier,
    panels: List<String> = listOf(),
    selectedPanel: Int,
    onPanelSelected: (Int) -> Unit,
    textSelectedColor: Color = Color(Config.colorConfig.textSelected),
    textDefaultColor: Color = Color(Config.colorConfig.textDefault),
    textStyle: TextStyle? = null,
    itemSpacing: Dp = 8.dp,
    textVerticalPadding: Dp = 8.dp,
    textHorizontalPadding: Dp = 6.dp,
) {

    val localTextStyle = LocalTextStyle.current
    val rememberedTextStyle: TextStyle =
        textStyle
            ?: remember(localTextStyle) {
                localTextStyle
                    //.applyCascadeTypographyTheme(TypographyApiModel(Typography.T3, FontWeights.SEMI_BOLD))
            }

    val itemsContent: LazyListScope.() -> Unit = {
        itemsIndexed(items = panels, key = { _, panel -> panel }) { index, panel ->
            PanelSelectorItem(
                panelName = panel,
                isSelected = index == selectedPanel,
                onClick = { onPanelSelected(index) },
                selectedColor = textSelectedColor,
                defaultColor = textDefaultColor,
                textStyle = rememberedTextStyle,
                verticalPadding = textVerticalPadding,
                horizontalPadding = textHorizontalPadding,
            )
        }
    }
    if (isLandScape()) {
        LazyColumn(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(itemSpacing, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = itemsContent,
        )
    } else {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(itemSpacing, Alignment.CenterHorizontally),
            content = itemsContent,
        )
    }
}

@Composable
private fun PanelSelectorItem(
    modifier: Modifier = Modifier,
    panelName: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    defaultColor: Color,
    textStyle: TextStyle,
    verticalPadding: Dp,
    horizontalPadding: Dp,
) {
    Text(
        text = panelName,
        color = if (isSelected) selectedColor else defaultColor,
        style = textStyle,
        modifier =
            modifier
                .clickable(onClick = onClick)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Landscape",
    device = "spec:parent=pixel_tablet,orientation=landscape",
)
@Composable
private fun PanelSelectorPreview() {
    PanelSelector(
        modifier = Modifier.padding(16.dp),
        panels = listOf("Presets", "Adjust", "Position", "Color"),
        selectedPanel = 1,
        onPanelSelected = {},
        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "Last Selected")
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Last Selected Landscape",
    device = "spec:parent=pixel_tablet,orientation=landscape",
)
@Composable
private fun PanelSelectorPreviewLast() {
    PanelSelector(
        modifier = Modifier.padding(16.dp),
        panels = listOf("Presets", "Adjust", "Position", "Color"),
        selectedPanel = 3,
        onPanelSelected = {},
        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    )
}
