package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.ChatViewModel
import com.example.ui.theme.Background
import com.example.ui.theme.Outline
import com.example.GeminiRepository

@Composable
fun MedTestApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Professor IA", style = MaterialTheme.typography.headlineMedium)
                    ChatContent()
                }
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = { scope.launch { drawerState.open() } }) {
                        Text("IA")
                    }
                }
            ) { paddingValues ->
                NavHost(navController = navController, startDestination = "selection", modifier = Modifier.padding(paddingValues)) {
                    composable("selection") { SelectionScreen(onNavigate = { navController.navigate(it) }) }
                    composable("student_dashboard") { StudentDashboardScreen(onNavigate = { navController.navigate(it) }) }
                    composable("test") { TestScreen(onNavigate = { navController.navigate(it) }) }
                    composable("chat") { ChatScreen(onNavigate = { navController.navigate(it) }) }
                }
            }
        }
    }
}

@Composable
fun SelectionScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { onNavigate("student_dashboard") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Estudante")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Teacher */ }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Professor")
        }
    }
}

@Composable
fun StudentDashboardScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Dashboard do Estudante", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigate("test") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Iniciar Prova")
        }
    }
}

@Composable
fun TestScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text("Prova", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onNavigate("chat") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Professor IA (Chat)")
        }
    }
}

@Composable
fun ChatContent(modifier: Modifier = Modifier) {
    val repository = remember { GeminiRepository() }
    val viewModel = remember { ChatViewModel(repository) }
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val textState = remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp)) {
            items(messages) { msg ->
                Card(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Outline)
                ) {
                    Text(text = msg, modifier = Modifier.padding(16.dp))
                }
            }
        }
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))) {
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
            )
            Button(onClick = {
                viewModel.sendMessage(textState.value)
                textState.value = ""
            }, modifier = Modifier.height(56.dp).padding(start = 8.dp), shape = RoundedCornerShape(20.dp)) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun ChatScreen(onNavigate: (String) -> Unit) {
    ChatContent()
}
