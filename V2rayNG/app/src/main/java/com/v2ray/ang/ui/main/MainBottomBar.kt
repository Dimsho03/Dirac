package com.v2ray.ang.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R

@Composable
fun MainBottomBar(
    displayText: String,
    isRunning: Boolean,
    isDarkTheme: Boolean,
    onAction: (MainAction) -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(22.dp)
    val cardColor = if (isRunning) {
        scheme.tertiaryContainer.copy(alpha = if (isDarkTheme) 0.46f else 0.58f)
    } else {
        scheme.surfaceContainerLow
    }
    val borderColor = if (isRunning) {
        scheme.tertiary.copy(alpha = 0.62f)
    } else {
        scheme.outlineVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(scheme.background)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Surface(
            shape = shape,
            color = cardColor,
            contentColor = scheme.onSurface,
            border = BorderStroke(1.dp, borderColor),
            shadowElevation = if (isRunning) 8.dp else 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onAction(MainAction.TestCurrentServer) }
                        .padding(vertical = 10.dp, horizontal = 2.dp)
                        .semantics { contentDescription = displayText },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                color = if (isRunning) scheme.tertiary
                                else scheme.onSurfaceVariant.copy(alpha = 0.45f),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isRunning) scheme.onTertiaryContainer else scheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    onClick = { onAction(MainAction.ToggleService) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isRunning) scheme.tertiary else scheme.primary,
                    contentColor = if (isRunning) scheme.onTertiary else scheme.onPrimary,
                    shadowElevation = if (isRunning) 6.dp else 0.dp,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = if (isRunning) {
                                painterResource(R.drawable.ic_stop_24dp)
                            } else {
                                painterResource(R.drawable.ic_play_24dp)
                            },
                            contentDescription = stringResource(
                                if (isRunning) R.string.acc_stop else R.string.acc_start
                            ),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
