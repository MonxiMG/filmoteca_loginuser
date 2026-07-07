@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

class FilmDataActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FILM_TITLE = "EXTRA_FILM_TITLE"
    }

    private val useCompose = true // XML = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filmTitle = intent.getStringExtra(EXTRA_FILM_TITLE) ?: "Película desconocida"
        val filmInfo = buildFilmInfo(filmTitle)

        if (useCompose) {
            setContent {
                // App bar + contenido con flecha HOME
                FilmDataScaffold(
                    title = filmInfo.title,
                    onBack = { finish() }, // ← flecha vuelve al listado
                ) {
                    FilmDataScreenCompose(
                        info = filmInfo,
                        onOpenImdb = { openUrl(filmInfo.imdbUrl) },
                        onEdit = { startActivity(Intent(this, FilmEditActivity::class.java)) },
                        onBackToMain = {
                            val i = Intent(this, FilmListActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            startActivity(i)
                        }
                    )
                }
            }
        } else {
            setContentView(R.layout.activity_film_data)
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun buildFilmInfo(title: String): FilmInfo {
        return if (title.contains("A", ignoreCase = true)) {
            FilmInfo(
                posterRes = R.drawable.pelicula_a,
                title = title,
                director = "Rio",
                year = "2022",
                genre = "Drama",
                format = "Blu-ray",
                imdbUrl = "https://www.imdb.com/title/tt1234567/"
            )
        } else {
            FilmInfo(
                posterRes = R.drawable.pelicula_b,
                title = title,
                director = "La Monja",
                year = "2021",
                genre = "Terror",
                format = "Online",
                imdbUrl = "https://www.imdb.com/title/tt7654321/"
            )
        }
    }
}

data class FilmInfo(
    val posterRes: Int,
    val title: String,
    val director: String,
    val year: String,
    val genre: String,
    val format: String,
    val imdbUrl: String
)

/* ---------------- Scaffold con App Bar (flecha HOME) ---------------- */

@Composable
private fun FilmDataScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            content()
        }
    }
}

/* ---------------- Pantalla Compose (contenido) ---------------- */

@Composable
fun FilmDataScreenCompose(
    info: FilmInfo,
    onOpenImdb: () -> Unit,
    onEdit: () -> Unit,
    onBackToMain: () -> Unit
) {
    val blackYellow = ButtonDefaults.buttonColors(
        containerColor = Color.Black,
        contentColor = Color.Yellow
    )

    var notes by rememberSaveable { mutableStateOf("") }
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cartel
        Image(
            painter = painterResource(id = info.posterRes),
            contentDescription = stringResource(R.string.about_image_cd),
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // Título (ya sale en AppBar, pero lo dejamos también si quieres)
        Text(text = info.title, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        // Ficha técnica
        LabeledValue(label = "Director", value = info.director)
        LabeledValue(label = "Año", value = info.year)
        LabeledValue(label = "Género", value = info.genre)
        LabeledValue(label = "Formato", value = info.format)

        Spacer(Modifier.height(16.dp))

        // Notas del usuario
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(stringResource(R.string.hint_notes)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(20.dp))

        // Botones (negro/amarillo)
        Button(
            onClick = onOpenImdb,
            colors = blackYellow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) { Text(text = stringResource(R.string.action_imdb)) }

        Button(
            onClick = onEdit,
            colors = blackYellow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) { Text(text = stringResource(R.string.action_edit)) }

        Button(
            onClick = onBackToMain,
            colors = blackYellow,
            modifier = Modifier.fillMaxWidth()
        ) { Text(text = stringResource(R.string.action_back_to_main)) }
    }
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:")
        Spacer(Modifier.width(8.dp))
        Text(text = value)
    }
}
