package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.UsageStatsRepository
import com.example.model.AppCategory
import com.example.model.AppSession
import com.example.model.DailyUsageSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

enum class TimelineSortOrder(val displayName: String) {
    MORNING_FIRST("Morning First (AM → PM)"),
    LATEST_FIRST("Latest First (PM → AM)"),
    LONGEST_FIRST("Longest Duration")
}

data class ScreenTimeUiState(
    val selectedDateMillis: Long = System.currentTimeMillis(),
    val summary: DailyUsageSummary? = null,
    val isLoading: Boolean = false,
    val hasPermission: Boolean = false,
    val isDemoMode: Boolean = true, // Default to demo so emulator showcases rich morning-to-night data immediately
    val selectedCategory: AppCategory? = null,
    val searchQuery: String = "",
    val selectedPeriodFilter: String? = null, // "Morning", "Afternoon", etc.
    val sortOrder: TimelineSortOrder = TimelineSortOrder.MORNING_FIRST,
    val selectedSessionForDetail: AppSession? = null,
    val activeTab: Int = 0 // 0: Timeline, 1: Apps, 2: 24h Strip
)

class ScreenTimeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsageStatsRepository(application)

    private val _uiState = MutableStateFlow(ScreenTimeUiState())
    val uiState: StateFlow<ScreenTimeUiState> = _uiState.asStateFlow()

    init {
        checkPermission()
        loadData()
    }

    fun checkPermission() {
        val granted = repository.hasUsageStatsPermission()
        _uiState.update { it.copy(hasPermission = granted) }
    }

    fun getUsageAccessIntent() = repository.getUsageAccessIntent()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val summary = repository.getDailyUsage(
                dateMillis = state.selectedDateMillis,
                forceDemo = state.isDemoMode || !state.hasPermission
            )
            _uiState.update {
                it.copy(
                    summary = summary,
                    isLoading = false
                )
            }
        }
    }

    fun selectDate(dateMillis: Long) {
        _uiState.update { it.copy(selectedDateMillis = dateMillis) }
        loadData()
    }

    fun goToPreviousDay() {
        val cal = Calendar.getInstance().apply {
            timeInMillis = _uiState.value.selectedDateMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        selectDate(cal.timeInMillis)
    }

    fun goToNextDay() {
        val cal = Calendar.getInstance().apply {
            timeInMillis = _uiState.value.selectedDateMillis
            add(Calendar.DAY_OF_YEAR, 1)
        }
        // Don't allow future days
        if (cal.timeInMillis <= System.currentTimeMillis() + 86400000L) {
            selectDate(cal.timeInMillis)
        }
    }

    fun goToToday() {
        selectDate(System.currentTimeMillis())
    }

    fun toggleDemoMode() {
        _uiState.update { it.copy(isDemoMode = !it.isDemoMode) }
        loadData()
    }

    fun setCategoryFilter(category: AppCategory?) {
        _uiState.update {
            it.copy(selectedCategory = if (it.selectedCategory == category) null else category)
        }
    }

    fun setPeriodFilter(periodTitle: String?) {
        _uiState.update {
            it.copy(selectedPeriodFilter = if (it.selectedPeriodFilter == periodTitle) null else periodTitle)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSortOrder(order: TimelineSortOrder) {
        _uiState.update { it.copy(sortOrder = order) }
    }

    fun selectSession(session: AppSession?) {
        _uiState.update { it.copy(selectedSessionForDetail = session) }
    }

    fun setActiveTab(tab: Int) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    // Filtered and sorted sessions
    val filteredSessions: StateFlow<List<AppSession>> = _uiState.combine(_uiState) { state, _ ->
        val all = state.summary?.sessions ?: emptyList()
        val query = state.searchQuery.trim().lowercase()

        var filtered = all.filter { session ->
            val matchesCategory = state.selectedCategory == null || session.category == state.selectedCategory
            val matchesSearch = query.isEmpty() ||
                    session.appName.lowercase().contains(query) ||
                    session.packageName.lowercase().contains(query)
            val matchesPeriod = if (state.selectedPeriodFilter == null) {
                true
            } else {
                val cal = Calendar.getInstance().apply { timeInMillis = session.startTimeMs }
                val h = cal.get(Calendar.HOUR_OF_DAY)
                when (state.selectedPeriodFilter) {
                    "Morning" -> h in 6..11
                    "Afternoon" -> h in 12..16
                    "Evening" -> h in 17..20
                    "Night" -> h >= 21 || h < 6
                    else -> true
                }
            }
            matchesCategory && matchesSearch && matchesPeriod
        }

        filtered = when (state.sortOrder) {
            TimelineSortOrder.MORNING_FIRST -> filtered.sortedBy { it.startTimeMs }
            TimelineSortOrder.LATEST_FIRST -> filtered.sortedByDescending { it.startTimeMs }
            TimelineSortOrder.LONGEST_FIRST -> filtered.sortedByDescending { it.durationMs }
        }

        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
