package com.example.app3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password) // Ensure correct layout is used

        auth = FirebaseAuth.getInstance()

        // Find views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnRecuperar = findViewById<Button>(R.id.btnRecuperar)
        val etEmailRecuperar = findViewById<EditText>(R.id.etEmailRecuperar)

        // Set click listeners
        btnBack.setOnClickListener {
            finish() // Go back to the previous screen
        }

        btnRecuperar.setOnClickListener {
            val email = etEmailRecuperar.text.toString().trim()
            if (email.isNotEmpty()) {
                verifyUserAndShowPassword(email)
            } else {
                showInfoDialog("Campo Vacío", "Por favor, ingrese su correo electrónico.")
            }
        }
    }

    private fun verifyUserAndShowPassword(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isUserFound = task.result?.signInMethods?.isNotEmpty() == true
                    if (isUserFound) {
                        val newPassword = UUID.randomUUID().toString().substring(0, 8)
                        val message = "El usuario existe en Firebase.\n\nTu nueva contraseña es: $newPassword\n\n(Nota: Esto es una simulación, la contraseña no se ha actualizado en Firebase.)"
                        showInfoDialog("Contraseña Recuperada (Simulación)", message, shouldFinish = true)
                    } else {
                        showInfoDialog("Usuario no encontrado", "No se encontró ningún usuario con el correo electrónico proporcionado.")
                    }
                } else {
                    showInfoDialog("Error de Verificación", "No se pudo verificar el correo electrónico. Inténtelo de nuevo.")
                }
            }
    }

    private fun showInfoDialog(title: String, message: String, shouldFinish: Boolean = false) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { _, _ ->
                if (shouldFinish) finish()
            }
            .setCancelable(false)
            .show()
    }
}
