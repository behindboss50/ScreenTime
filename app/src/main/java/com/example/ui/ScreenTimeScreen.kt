package com.example.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppCategory
import com.example.model.AppSession
import com.example.ui.components.AppBreakdownCard
import com.example.ui.components.DailySummaryHeader
import com.example.ui.components.SessionDetailSheet
import com.example.ui.components.SessionItemCard
import com.example.ui.components.TimelineBar
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimeScreen(
    viewModel: ScreenTimeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredSessions by viewModel.filteredSessions.collectAsStateWithLifecycle()

    var showSearchRow by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg),
        topBar = {
            Column(modifier = Modifier.background(DarkBg)) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(AccentEmerald)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "True Screen Time",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { showSearchRow = !showSearchRow },
                            modifier = Modifier.testTag("search_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (showSearchRow) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (showSearchRow) AccentCyan else TextPrimary
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { showSortMenu = true },
                                modifier = Modifier.testTag("sort_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Sort",
                                    tint = TextPrimary
                                )
                            }

                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false },
                                modifier = Modifier.background(DarkSurfaceElevated)
                            ) {
                                TimelineSortOrder.entries.forEach { order ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = order.displayName,
                                                color = if (uiState.sortOrder == order) AccentCyan else TextPrimary,
                                                fontWeight = if (uiState.sortOrder == order) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            viewModel.setSortOrder(order)
                                            showSortMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { viewModel.loadData() },
                            modifier = Modifier.testTag("refresh_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkBg
                    )
                )

                // Expandable Search Bar
                AnimatedVisibility(visible = showSearchRow) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search by app name...", color = TextMuted) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted)
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkSurface,
                                unfocusedContainerColor = DarkSurface,
                                focusedBorderColor = AccentCyan,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_text_field")
                        )
                    }
                }

                // Tabs: Timeline, Apps, 24h Hourly
                TabRow(
                    selectedTabIndex = uiState.activeTab,
                    containerColor = DarkBg,
                    contentColor = TextPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[uiState.activeTab]),
                            color = AccentCyan,
                            height = 3.dp
                        )
                    },
                    divider = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(DarkBorder)
                        )
                    }
                ) {
                    Tab(
                        selected = uiState.activeTab == 0,
                        onClick = { viewModel.setActiveTab(0) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Timeline", fontWeight = FontWeight.Bold)
                            }
                        },
                        selectedContentColor = AccentCyan,
                        unselectedContentColor = TextSecondary,
                        modifier = Modifier.testTag("tab_timeline")
                    )

                    Tab(
                        selected = uiState.activeTab == 1,
                        onClick = { viewModel.setActiveTab(1) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Layers, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Apps (${uiState.summary?.appSummaries?.size ?: 0})", fontWeight = FontWeight.Bold)
                            }
                        },
                        selectedContentColor = AccentCyan,
                        unselectedContentColor = TextSecondary,
                        modifier = Modifier.testTag("tab_apps")
                    )

                    Tab(
                        selected = uiState.activeTab == 2,
                        onClick = { viewModel.setActiveTab(2) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.BarChart, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("24h Breakdown", fontWeight = FontWeight.Bold)
                            }
                        },
                        selectedContentColor = AccentCyan,
                        unselectedContentColor = TextSecondary,
                        modifier = Modifier.testTag("tab_breakdown")
                    )
                }
            }
        },
        containerColor = DarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            when (uiState.activeTab) {
                0 -> TimelineTabContent(
                    uiState = uiState,
                    sessions = filteredSessions,
                    viewModel = viewModel,
                    onRequestPermission = {
                        context.startActivity(viewModel.getUsageAccessIntent())
                    }
                )
                1 -> AppsTabContent(
                    uiState = uiState,
                    onAppClick = { appName ->
                        viewModel.setSearchQuery(appName)
                        viewModel.setActiveTab(0) // Switch to timeline filtered to that app
                    }
                )
                2 -> HourlyTabContent(uiState = uiState)
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentCyan)
                }
            }
        }
    }

    // Session Detail Bottom Sheet
    if (uiState.selectedSessionForDetail != null) {
        SessionDetailSheet(
            session = uiState.selectedSessionForDetail,
            dailySummary = uiState.summary,
            onDismiss = { viewModel.selectSession(null) },
            onFilterByApp = { appName ->
                viewModel.setSearchQuery(appName)
            }
        )
    }
}

@Composable
private fun TimelineTabContent(
    uiState: ScreenTimeUiState,
    sessions: List<AppSession>,
    viewModel: ScreenTimeViewModel,
    onRequestPermission: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Daily Summary Header & Quick Stats
        item {
            DailySummaryHeader(
                summary = uiState.summary,
                isDemoMode = uiState.isDemoMode,
                hasPermission = uiState.hasPermission,
                onPreviousDay = { viewModel.goToPreviousDay() },
                onNextDay = { viewModel.goToNextDay() },
                onToday = { viewModel.goToToday() },
                onToggleDemo = { viewModel.toggleDemoMode() },
                onRequestPermission = onRequestPermission
            )
        }

        // 24h Activity Strip Panorama & Period filter
        item {
            TimelineBar(
                sessions = uiState.summary?.sessions ?: emptyList(),
                selectedPeriod = uiState.selectedPeriodFilter,
                onPeriodSelected = { viewModel.setPeriodFilter(it) }
            )
        }

        // Category Filter Chips
        item {
            CategoryFilterChipsRow(
                selectedCategory = uiState.selectedCategory,
                onSelectCategory = { viewModel.setCategoryFilter(it) }
            )
        }

        // Active Filter Summary Info Bar (if filter active)
        if (uiState.selectedCategory != null || uiState.searchQuery.isNotEmpty() || uiState.selectedPeriodFilter != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${sessions.size} sessions matching filter",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan
                    )

                    Text(
                        text = "Clear Filters",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier
                            .clickable {
                                viewModel.setCategoryFilter(null)
                                viewModel.setPeriodFilter(null)
                                viewModel.setSearchQuery("")
                            }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Timeline Header Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Session Timeline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = uiState.sortOrder.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        // Empty State
        if (sessions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "⏳", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No sessions found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Try clearing filters or switching date",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            // Render Chronological Sessions with Start and End Times
            itemsIndexed(
                items = sessions,
                key = { _, session -> session.id }
            ) { index, session ->
                SessionItemCard(
                    session = session,
                    isFirstInList = index == 0,
                    isLastInList = index == sessions.lastIndex,
                    onSessionClick = { viewModel.selectSession(it) }
                )
            }
        }
    }
}

@Composable
private fun AppsTabContent(
    uiState: ScreenTimeUiState,
    onAppClick: (String) -> Unit
) {
    val apps = uiState.summary?.appSummaries ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Most Used Apps Today",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Tap any app to filter and see its full morning-to-night session timeline",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(apps.size, key = { apps[it].packageName }) { idx ->
            AppBreakdownCard(
                app = apps[idx],
                onAppClick = onAppClick
            )
        }
    }
}

@Composable
private fun HourlyTabContent(
    uiState: ScreenTimeUiState
) {
    val hourlyList = uiState.summary?.hourlyDistribution ?: emptyList()
    val maxHourlyTime = (hourlyList.maxOfOrNull { it.screenTimeMs } ?: 1L).coerceAtLeast(1L)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "24-Hour Screen Time Heatmap",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Track phone usage distribution hour-by-hour from morning to night",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(hourlyList.size) { idx ->
            val hourItem = hourlyList[idx]
            val pct = (hourItem.screenTimeMs.toFloat() / maxHourlyTime.toFloat()).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(90.dp)
                    ) {
                        Text(
                            text = hourItem.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    // Progress bar representing hour's screen time
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFF1B2436))
                    ) {
                        if (pct > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(pct)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(
                                        if (hourItem.screenTimeMs > 30 * 60 * 1000L)
                                            AccentEmerald
                                        else
                                            AccentCyan
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = if (hourItem.screenTimeMs > 0)
                            AppSession.formatDuration(hourItem.screenTimeMs)
                        else
                            "—",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hourItem.screenTimeMs > 0) TextPrimary else TextMuted,
                        modifier = Modifier.width(60.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterChipsRow(
    selectedCategory: AppCategory?,
    onSelectCategory: (AppCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" chip
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (selectedCategory == null) AccentCyan else DarkSurface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (selectedCategory == null) AccentCyan else DarkBorder
            ),
            modifier = Modifier
                .clickable { onSelectCategory(null) }
                .testTag("filter_category_all")
        ) {
            Text(
                text = "All Categories",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedCategory == null) Color.Black else TextSecondary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        AppCategory.entries.forEach { category ->
            val isSelected = selectedCategory == category
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (isSelected) category.color else DarkSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) category.color else DarkBorder
                ),
                modifier = Modifier
                    .clickable { onSelectCategory(category) }
                    .testTag("filter_category_${category.name}")
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else TextSecondary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
