package com.example.app3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.app3.model.News
import com.example.app3.ui.theme.App3Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddNewsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddNewsScreen(onBack = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewsScreen(onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = Firebase.firestore

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Noticia") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text("Resumen") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (title.isBlank() || summary.isBlank() || content.isBlank()) {
                        Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        isSubmitting = true
                        val news = News(
                            title = title,
                            summary = summary,
                            content = content
                        )
                        db.collection("news")
                            .add(news)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Noticia agregada exitosamente", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error al agregar noticia: ${e.message}", Toast.LENGTH_SHORT).show()
                                isSubmitting = false
                            }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar Noticia")
                }
            }
        }
    }
}
