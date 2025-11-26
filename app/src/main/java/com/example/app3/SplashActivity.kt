package com.example.app3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app3.ui.theme.App3Theme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(
                        onTimeout = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit, modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds delay
        onTimeout()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        // Using ShoppingCart as the app logo for the bakery (PanAppDeria)
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
