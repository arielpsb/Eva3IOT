package com.example.app3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.app3.model.News
import com.example.app3.ui.theme.App3Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class NewsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    val context = LocalContext.current
    val db = Firebase.firestore
    val auth = Firebase.auth
    var newsList by remember { mutableStateOf<List<News>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("news")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("NewsScreen", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val newsWithIds = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(News::class.java)?.copy(id = doc.id)
                    }
                    newsList = newsWithIds
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Noticias") },
                actions = {
                    TextButton(onClick = {
                        auth.signOut()
                        val intent = Intent(context, MainActivity::class.java)
                        // Clear the back stack so the user cannot return to the news screen without logging in
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, AddNewsActivity::class.java))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add News")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Added a top spacer to separate content from the top bar visual
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(newsList) { news ->
                NewsItem(news) {
                    val intent = Intent(context, NewsDetailActivity::class.java)
                    intent.putExtra("news_id", news.id)
                    intent.putExtra("news_title", news.title)
                    intent.putExtra("news_content", news.content)
                    context.startActivity(intent)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NewsItem(news: News, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = news.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = news.summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
