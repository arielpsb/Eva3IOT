package com.example.app3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        
        // Check if user is already signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, go to NewsActivity
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
        }

        enableEdgeToEdge()
        setContent {
            App3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(modifier = Modifier.padding(innerPadding), auth = auth)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, auth: FirebaseAuth) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Message
        Text(
            text = "Bienvenido a PanAppDeria",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
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
                if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
                } else {
                    isLoading = true
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, NewsActivity::class.java)
                                context.startActivity(intent)
                                if (context is ComponentActivity) {
                                    context.finish()
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(context, "Fallo en autenticación: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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
                Text("Ingresar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Olvidaste tu contraseña? Recupérala aquí",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                context.startActivity(Intent(context, RecoverPasswordActivity::class.java))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                context.startActivity(Intent(context, RegisterAccountActivity::class.java))
            }
        )
    }
}
