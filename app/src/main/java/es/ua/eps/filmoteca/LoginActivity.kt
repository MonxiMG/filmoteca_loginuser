package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga de la pantalla de login.
        setContentView(R.layout.activity_login)

        // Referencia al botón de inicio de sesión.
        val btnGoogleSignIn = findViewById<Button>(R.id.btnGoogleSignIn)

        // Apertura temporal de la pantalla principal de la Filmoteca.
        // Más adelante se sustituirá por el inicio de sesión real con Google.
        btnGoogleSignIn.setOnClickListener {
            val intent = Intent(this, FilmListActivity::class.java)
            startActivity(intent)

            // Cierre de LoginActivity para evitar volver al login con el botón Atrás.
            finish()
        }
    }
}