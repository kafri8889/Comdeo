package com.anafthdev.comdeo.foundation.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun ComdeoDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    if (expanded) {
        Popup(
            onDismissRequest = onDismissRequest,
            alignment = Alignment.TopEnd,
            properties = PopupProperties(focusable = true)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = RoundedCornerShape(12),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(content = content)
            }
        }
    }
}

@Composable
fun ComdeoDropdownMenuItem(
    onClick: () -> Unit,
    title: @Composable () -> Unit
) {

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(8.dp)
            .sizeIn(minWidth = 112.dp, minHeight = 40.dp)
    ) {
        title()
    }
}
