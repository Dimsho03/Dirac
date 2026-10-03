package com.v2ray.ang.ui.dirac

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.v2ray.ang.R
import com.v2ray.ang.ui.main.MainAction
import com.v2ray.ang.ui.main.MainDestination
import com.v2ray.ang.ui.main.MainViewModel
import com.v2ray.ang.ui.main.ServerRowUiModel

@Composable
fun DiracHomeScreen(
    mainViewModel: MainViewModel,
    onAction: (MainAction) -> Unit,
    onNavigate: (MainDestination) -> Unit,
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by mainViewModel.isLoading.collectAsStateWithLifecycle()

    val selectedGroupId = uiState.selectedGroupId
        .takeIf { selected -> uiState.groups.any { it.id == selected } }
        ?: uiState.groups.firstOrNull()?.id
        .orEmpty()

    val groupStateFlow = remember(selectedGroupId) {
        mainViewModel.serverGroupState(selectedGroupId)
    }
    val groupState by groupStateFlow.collectAsStateWithLifecycle()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState.groups, uiState.selectedGroupId) {
        val selectedExists = uiState.groups.any { it.id == uiState.selectedGroupId }
        if (!selectedExists) {
            uiState.groups.firstOrNull()?.let { onAction(MainAction.SelectGroup(it.id)) }
        }
    }

    val selectedRow = groupState.rows.firstOrNull { it.guid == uiState.selectedGuid }
    val statusText = mainViewModel.formatStatus(uiState.status)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DiracBackground,
        contentColor = DiracText,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 18.dp),
        ) {
            DiracHeader(
                isLoading = isLoading,
                onSubscriptions = { onNavigate(MainDestination.Subscriptions) },
                onSettings = { onNavigate(MainDestination.Settings) },
            )

            Spacer(Modifier.height(12.dp))

            ConnectionHero(
                isRunning = uiState.isRunning,
                statusText = statusText,
                selectedName = selectedRow?.remarks,
                selectedDetails = selectedRow?.typeDescription,
                onToggle = { onAction(MainAction.ToggleService) },
            )

            Spacer(Modifier.height(14.dp))

            QuickActions(
                isRunning = uiState.isRunning,
                onTest = { onAction(MainAction.TestCurrentServer) },
                onQr = { onAction(MainAction.ImportQRcode) },
                onClipboard = { onAction(MainAction.ImportClipboard) },
            )

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Profiles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = groupState.rows.size.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = DiracMuted,
                )
            }

            if (uiState.groups.size > 1) {
                Spacer(Modifier.height(12.dp))
                DiracGroupSelector(
                    groups = uiState.groups.map { it.id to it.remarks },
                    selectedGroupId = selectedGroupId,
                    onSelected = { onAction(MainAction.SelectGroup(it)) },
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onAction(MainAction.Search(it))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.menu_item_search),
                        color = DiracMuted,
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_24dp),
                        contentDescription = null,
                        tint = DiracMuted,
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DiracSurface,
                    unfocusedContainerColor = DiracSurface,
                    disabledContainerColor = DiracSurface,
                    focusedIndicatorColor = DiracSage.copy(alpha = 0.72f),
                    unfocusedIndicatorColor = DiracBorder,
                    cursorColor = DiracSage,
                    focusedTextColor = DiracText,
                    unfocusedTextColor = DiracText,
                ),
            )

            Spacer(Modifier.height(12.dp))

            if (groupState.rows.isEmpty()) {
                EmptyProfiles(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onImport = { onAction(MainAction.ImportClipboard) },
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = groupState.rows,
                        key = { it.guid },
                    ) { row ->
                        DiracProfileCard(
                            row = row,
                            selected = row.guid == uiState.selectedGuid,
                            onSelect = { onAction(MainAction.SelectServer(row.guid)) },
                            onEdit = { onAction(MainAction.EditServer(row.guid, row.profile)) },
                        )
                    }
                    item { Spacer(Modifier.height(10.dp)) }
                }
            }
        }
    }
}

@Composable
private fun DiracHeader(
    isLoading: Boolean,
    onSubscriptions: () -> Unit,
    onSettings: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "DIRAC",
                fontSize = 23.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "PRIVATE NETWORK",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.4.sp,
                color = DiracMuted,
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(18.dp)
                    .padding(1.dp),
                strokeWidth = 2.dp,
                color = DiracSage,
            )
            Spacer(Modifier.width(8.dp))
        }

        HeaderIcon(
            iconRes = R.drawable.ic_subscriptions_24dp,
            contentDescription = stringResource(R.string.title_sub_setting),
            onClick = onSubscriptions,
        )
        Spacer(Modifier.width(8.dp))
        HeaderIcon(
            iconRes = R.drawable.ic_settings_24dp,
            contentDescription = stringResource(R.string.title_settings),
            onClick = onSettings,
        )
    }
}

@Composable
private fun HeaderIcon(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(42.dp),
        shape = RoundedCornerShape(14.dp),
        color = DiracSurface,
        border = BorderStroke(1.dp, DiracBorder),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = DiracText,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ConnectionHero(
    isRunning: Boolean,
    statusText: String,
    selectedName: String?,
    selectedDetails: String?,
    onToggle: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = if (isRunning) DiracSage.copy(alpha = 0.075f) else DiracSurface,
        border = BorderStroke(
            1.dp,
            if (isRunning) DiracSage.copy(alpha = 0.56f) else DiracBorder,
        ),
        shadowElevation = if (isRunning) 8.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                onClick = onToggle,
                modifier = Modifier.size(104.dp),
                shape = CircleShape,
                color = if (isRunning) DiracSage else DiracSurfaceRaised,
                contentColor = if (isRunning) Color(0xFF0F1612) else DiracText,
                border = BorderStroke(
                    1.dp,
                    if (isRunning) DiracSage else DiracBorder,
                ),
                shadowElevation = if (isRunning) 12.dp else 2.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            if (isRunning) R.drawable.ic_stop_24dp
                            else R.drawable.ic_play_24dp
                        ),
                        contentDescription = stringResource(
                            if (isRunning) R.string.acc_stop else R.string.acc_start
                        ),
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (isRunning) DiracSage else DiracMuted.copy(alpha = 0.45f),
                            shape = CircleShape,
                        ),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isRunning) {
                        stringResource(R.string.connection_connected)
                    } else {
                        stringResource(R.string.connection_not_connected)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = selectedName ?: "No profile selected",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = DiracText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (!selectedDetails.isNullOrBlank()) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = selectedDetails,
                    style = MaterialTheme.typography.bodySmall,
                    color = DiracMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (statusText.isNotBlank() &&
                statusText != stringResource(R.string.connection_connected) &&
                statusText != stringResource(R.string.connection_not_connected)
            ) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = DiracMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun QuickActions(
    isRunning: Boolean,
    onTest: () -> Unit,
    onQr: () -> Unit,
    onClipboard: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        QuickAction(
            modifier = Modifier.weight(1f),
            iconRes = R.drawable.ic_flash_on_24dp,
            label = stringResource(R.string.title_real_ping_all_server),
            enabled = isRunning,
            onClick = onTest,
        )
        QuickAction(
            modifier = Modifier.weight(1f),
            iconRes = R.drawable.ic_scan_24dp,
            label = stringResource(R.string.menu_item_import_config_qrcode),
            onClick = onQr,
        )
        QuickAction(
            modifier = Modifier.weight(1f),
            iconRes = R.drawable.ic_copy,
            label = stringResource(R.string.menu_item_import_config_clipboard),
            onClick = onClipboard,
        )
    }
}

@Composable
private fun QuickAction(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(76.dp)
            .alpha(if (enabled) 1f else 0.42f),
        shape = RoundedCornerShape(18.dp),
        color = DiracSurface,
        border = BorderStroke(1.dp, DiracBorder),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 11.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = DiracText,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = DiracMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DiracGroupSelector(
    groups: List<Pair<String, String>>,
    selectedGroupId: String,
    onSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        groups.forEach { (id, remarks) ->
            val selected = id == selectedGroupId
            Surface(
                onClick = { onSelected(id) },
                shape = RoundedCornerShape(14.dp),
                color = if (selected) DiracText else DiracSurface,
                contentColor = if (selected) DiracBackground else DiracText,
                border = BorderStroke(
                    1.dp,
                    if (selected) DiracText else DiracBorder,
                ),
            ) {
                Text(
                    text = remarks.ifBlank { "Default" },
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun DiracProfileCard(
    row: ServerRowUiModel,
    selected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
) {
    Surface(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) DiracSage.copy(alpha = 0.075f) else DiracSurface,
        border = BorderStroke(
            1.dp,
            if (selected) DiracSage.copy(alpha = 0.62f) else DiracBorder,
        ),
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 14.dp, end = 10.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (selected) DiracSage else DiracBorder,
                        shape = CircleShape,
                    ),
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = row.remarks,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = row.statistics,
                    style = MaterialTheme.typography.bodySmall,
                    color = DiracMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(7.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = row.typeDescription,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) DiracSage else DiracMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    if (row.testDelayMillis != 0L) {
                        Text(
                            text = if (row.testDelayMillis > 0L) {
                                "${row.testDelayMillis} ms"
                            } else {
                                "—"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (row.testDelayMillis > 0L) DiracSage else MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            IconButton(onClick = onEdit) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit_24dp),
                    contentDescription = stringResource(R.string.acc_edit),
                    tint = DiracMuted,
                    modifier = Modifier.size(19.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyProfiles(
    modifier: Modifier,
    onImport: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No profiles",
                style = MaterialTheme.typography.titleMedium,
                color = DiracText,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.menu_item_import_config_clipboard),
                style = MaterialTheme.typography.bodySmall,
                color = DiracMuted,
            )
            Spacer(Modifier.height(14.dp))
            Surface(
                onClick = onImport,
                shape = RoundedCornerShape(16.dp),
                color = DiracText,
                contentColor = DiracBackground,
            ) {
                Text(
                    text = stringResource(R.string.menu_item_import_config_clipboard),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
