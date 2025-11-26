package com.example.app3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog

class RegisterAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        // Find views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmailRegistro)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegistro)

        // Set click listeners
        btnBack.setOnClickListener {
            // Finish the activity and go back to the previous screen
            finish()
        }

        btnRegistrar.setOnClickListener {
            val name = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Here you would normally implement Firebase registration logic
                // For this simulation, we'll just show a success dialog
                showRegistrationSuccessDialog()
            } else {
                showInfoDialog("Campos Incompletos", "Por favor, rellene todos los campos.")
            }
        }
    }

    private fun showRegistrationSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Registro Exitoso (Simulación)")
            .setMessage("La cuenta ha sido registrada correctamente.")
            .setPositiveButton("Aceptar") { _, _ ->
                finish() // Go back to the main activity
            }
            .setCancelable(false)
            .show()
    }

    private fun showInfoDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .setCancelable(false)
            .show()
    }
}
