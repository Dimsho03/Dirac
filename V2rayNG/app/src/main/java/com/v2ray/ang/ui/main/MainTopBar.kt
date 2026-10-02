package com.v2ray.ang.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.AppTopBar
import com.v2ray.ang.ui.compose.verticalScrollbar

@Composable
fun MainTopBar(
    isLoading: Boolean,
    showSearch: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    onSearchToggle: (Boolean) -> Unit,
    onMenuClick: () -> Unit,
    onAction: (MainAction) -> Unit,
    onMoreMenuAction: (MainMoreMenuAction) -> Unit
) {
    var showImportMenu by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val importMenuScrollState = rememberScrollState()
    val moreMenuScrollState = rememberScrollState()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val maxMenuHeight =
        LocalConfiguration.current.screenHeightDp.dp - statusBarHeight - navBarHeight - 20.dp

    AppTopBar(
        title = "Dirac",
        onBackClick = {},
        isLoading = isLoading,
        isSearchActive = showSearch,
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        onSearchClose = onSearchClose,
        searchPlaceholder = stringResource(R.string.menu_item_search),
        navigationIcon = {
            if (showSearch) {
                DiracTopActionButton(
                    onClick = onSearchClose,
                    iconRes = R.drawable.ic_arrow_back_24dp,
                    contentDescription = stringResource(R.string.acc_back),
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else {
                DiracBrandButton(
                    onClick = onMenuClick,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        actions = {
            if (!showSearch) {
                DiracTopActionButton(
                    onClick = { onSearchToggle(true) },
                    iconRes = R.drawable.ic_search_24dp,
                    contentDescription = stringResource(R.string.acc_search)
                )
            }
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                DiracTopActionButton(
                    onClick = { showImportMenu = true },
                    iconRes = R.drawable.ic_add_24dp,
                    contentDescription = stringResource(R.string.acc_add)
                )
                DropdownMenu(
                    expanded = showImportMenu,
                    onDismissRequest = { showImportMenu = false },
                    scrollState = importMenuScrollState,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .heightIn(max = maxMenuHeight)
                        .verticalScrollbar(importMenuScrollState)
                ) {
                    ImportMenuContent(
                        onAction = { action ->
                            showImportMenu = false
                            onAction(action)
                        }
                    )
                }
            }
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                DiracTopActionButton(
                    onClick = { showMenu = true },
                    iconRes = R.drawable.ic_more_vert_24dp,
                    contentDescription = stringResource(R.string.acc_more),
                    modifier = Modifier.padding(end = 8.dp)
                )
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    scrollState = moreMenuScrollState,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .heightIn(max = maxMenuHeight)
                        .verticalScrollbar(moreMenuScrollState)
                ) {
                    MoreMenuContent { action ->
                        showMenu = false
                        onMoreMenuAction(action)
                    }
                }
            }
        }
    )
}

@Composable
private fun DiracBrandButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "|D>",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DiracTopActionButton(
    onClick: () -> Unit,
    iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
