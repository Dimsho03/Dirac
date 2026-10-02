package com.v2ray.ang.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.AppDivider
import com.v2ray.ang.ui.compose.verticalScrollbar

enum class MainDestination(@DrawableRes val iconRes: Int, @StringRes val labelRes: Int) {
    Subscriptions(R.drawable.ic_subscriptions_24dp, R.string.title_sub_setting),
    PerAppProxy(R.drawable.ic_per_apps_24dp, R.string.per_app_proxy_settings),
    Routing(R.drawable.ic_routing_24dp, R.string.routing_settings_title),
    UserAssets(R.drawable.ic_file_24dp, R.string.title_user_asset_setting),
    Settings(R.drawable.ic_settings_24dp, R.string.title_settings),
    Promotion(R.drawable.ic_promotion_24dp, R.string.title_pref_promotion),
    Logcat(R.drawable.ic_logcat_24dp, R.string.title_logcat),
    CheckUpdate(R.drawable.ic_check_update_24dp, R.string.update_check_for_update),
    BackupRestore(R.drawable.ic_restore_24dp, R.string.title_configuration_backup_restore),
    About(R.drawable.ic_about_24dp, R.string.title_about)
}

private val primaryDrawerItems = listOf(
    MainDestination.Subscriptions,
    MainDestination.PerAppProxy,
    MainDestination.Routing,
    MainDestination.UserAssets,
    MainDestination.Settings
)

private val drawerItems = primaryDrawerItems + listOf(
    MainDestination.Promotion,
    MainDestination.Logcat,
    MainDestination.CheckUpdate,
    MainDestination.BackupRestore,
    MainDestination.About
)

@Composable
fun MainDrawerContent(drawerState: DrawerState, onNavigate: (MainDestination) -> Unit) {
    val drawerScrollState = rememberScrollState()
    val scheme = MaterialTheme.colorScheme

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(0.82f),
        drawerContainerColor = scheme.background,
        drawerContentColor = scheme.onBackground,
        drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(drawerScrollState)
                .verticalScrollbar(drawerScrollState)
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = scheme.surfaceContainerLow,
                contentColor = scheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = scheme.primary,
                        contentColor = scheme.onPrimary,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "|D>",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Dirac",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "VPN",
                            style = MaterialTheme.typography.labelMedium,
                            color = scheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            drawerItems.forEachIndexed { index, item ->
                if (index == primaryDrawerItems.size) {
                    Spacer(modifier = Modifier.height(6.dp))
                    AppDivider()
                    Spacer(modifier = Modifier.height(6.dp))
                }
                DiracDrawerItem(
                    destination = item,
                    onClick = { onNavigate(item) }
                )
            }
        }
    }
}

@Composable
private fun DiracDrawerItem(
    destination: MainDestination,
    onClick: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = scheme.surfaceContainerLow,
        contentColor = scheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = scheme.surfaceContainerHigh,
                contentColor = scheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(destination.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(destination.labelRes),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
