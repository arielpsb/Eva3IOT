package com.example.app3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app3.ui.theme.App3Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterAccountActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            App3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterAccountScreen(modifier = Modifier.padding(innerPadding), auth = auth)
                }
            }
        }
    }
}

@Composable
fun RegisterAccountScreen(modifier: Modifier = Modifier, auth: FirebaseAuth) {
    var username by remember { mutableStateOf("") } // We can store this in user profile later if needed
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registrar Cuenta", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                     Toast.makeText(context, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                // Sign in success
                                Toast.makeText(context, "Cuenta registrada exitosamente", Toast.LENGTH_SHORT).show()
                                // Go back to previous activity (Login) or directly to NewsActivity
                                if (context is ComponentActivity) {
                                    context.finish()
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Fallo en registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
             if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Registrar")
            }
        }
    }
}
