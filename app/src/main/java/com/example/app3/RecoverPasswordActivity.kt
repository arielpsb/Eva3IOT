package com.example.app3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.app3.ui.theme.App3Theme

class RecoverPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecoverPasswordScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RecoverPasswordScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { 
                    showDialog = false 
                    // Go back to previous activity (MainActivity)
                    if (context is ComponentActivity) {
                        context.finish()
                    }
                }) {
                    Text("OK")
                }
            },
            title = { Text("Recuperación") },
            text = { Text("Se ha enviado un correo de recuperación a $email") }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Recuperar Clave", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar")
        }
    }
}
