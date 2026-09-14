package com.fitness.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.fitness.myapplication.data.TruckerLogDatabase
import com.fitness.myapplication.navigation.Destination
import com.fitness.myapplication.ui.screens.DashboardScreen
import com.fitness.myapplication.ui.screens.EditLogScreen
import com.fitness.myapplication.ui.screens.HistoryScreen
import com.fitness.myapplication.ui.theme.MyApplicationTheme
import com.fitness.myapplication.ui.viewmodel.TruckerLogViewModel
import com.fitness.myapplication.ui.viewmodel.TruckerLogViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TruckerLogApp()
            }
        }
    }
}

@Composable
fun TruckerLogApp() {
    val context = LocalContext.current
    val database = remember { TruckerLogDatabase.getDatabase(context) }
    val viewModel: TruckerLogViewModel = viewModel(
        factory = TruckerLogViewModelFactory(database.truckerLogDao())
    )

    val backStack = remember { mutableStateListOf<Any>(Destination.Dashboard) }

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Destination.Dashboard -> NavEntry(key) {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToHistory = { backStack.add(Destination.History) }
                    )
                }
                is Destination.History -> NavEntry(key) {
                    HistoryScreen(
                        viewModel = viewModel,
                        onEditLog = { logId -> backStack.add(Destination.EditLog(logId)) },
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                is Destination.EditLog -> NavEntry(key) {
                    EditLogScreen(
                        logId = key.logId ?: 0,
                        viewModel = viewModel,
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }
                else -> NavEntry(Unit) { }
            }
        }
    )
}
