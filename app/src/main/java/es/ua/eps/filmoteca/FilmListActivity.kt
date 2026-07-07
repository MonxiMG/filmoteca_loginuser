package es.ua.eps.filmoteca

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class FilmListActivity : AppCompatActivity() {

    private val useCompose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (useCompose) {
            setContent {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FilmListScreen(
                        onSeeA = {
                            startActivity(
                                Intent(this, FilmDataActivity::class.java)
                                    .putExtra(FilmDataActivity.EXTRA_FILM_TITLE, "Película A")
                            )
                        },
                        onSeeB = {
                            startActivity(
                                Intent(this, FilmDataActivity::class.java)
                                    .putExtra(FilmDataActivity.EXTRA_FILM_TITLE, "Película B")
                            )
                        },
                        onAbout = { startActivity(Intent(this, AboutActivity::class.java)) },
                        onExit = { finishAffinity() }
                    )
                }
            }
        } else {
            setContentView(R.layout.activity_film_list)

            // --- Toolbar como AppBar ---
            findViewById<MaterialToolbar?>(R.id.toolbar)?.let { tb ->
                setSupportActionBar(tb)

                // Oculta cualquier título en la AppBar
                supportActionBar?.setDisplayShowTitleEnabled(false)
                supportActionBar?.title = null
                tb.title = ""

                // Fuerza el menú en la propia Toolbar (para que siempre se vea/click)
                tb.menu.clear()
                tb.inflateMenu(R.menu.menu_film_list)
                tb.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_add -> { addDefaultFilm(); true }
                        R.id.action_about -> { startActivity(Intent(this, AboutActivity::class.java)); true }
                        else -> false
                    }
                }
            }

            val listView = findViewById<ListView>(R.id.lvFilms)
            val adapter = FilmAdapter(this, FilmDataSource.films)
            listView.adapter = adapter

            listView.setOnItemClickListener { _, _, position, _ ->
                startActivity(
                    Intent(this, FilmDataActivity::class.java)
                        .putExtra("EXTRA_FILM_INDEX", position)
                )
            }

            activarBorradoMultiple(listView, adapter)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_film_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> { addDefaultFilm(); true }
            R.id.action_about -> { startActivity(Intent(this, AboutActivity::class.java)); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addDefaultFilm() {
        val nueva = Film(
            title = "Nueva película",
            director = "Desconocido",
            year = 2025,
            genre = "Drama",
            format = "Blu-ray",
            imdbUrl = "https://www.imdb.com/",
            posterRes = R.drawable.ic_launcher_foreground,
            notes = ""
        )
        FilmDataSource.films.add(nueva)
        (findViewById<ListView>(R.id.lvFilms).adapter as? FilmAdapter)?.notifyDataSetChanged()
    }

    private fun activarBorradoMultiple(listView: ListView, adapter: FilmAdapter) {
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        listView.setMultiChoiceModeListener(object : android.widget.AbsListView.MultiChoiceModeListener {
            private val seleccionadas = mutableSetOf<Int>()
            override fun onCreateActionMode(mode: android.view.ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.menu_context_delete, menu); seleccionadas.clear(); return true
            }
            override fun onPrepareActionMode(mode: android.view.ActionMode, menu: Menu) = false
            override fun onItemCheckedStateChanged(mode: android.view.ActionMode, position: Int, id: Long, checked: Boolean) {
                if (checked) seleccionadas.add(position) else seleccionadas.remove(position)
                mode.title = "${seleccionadas.size} seleccionada(s)"
            }
            override fun onActionItemClicked(mode: android.view.ActionMode, item: MenuItem): Boolean {
                return if (item.itemId == R.id.action_delete) {
                    val indices = seleccionadas.toList().sortedDescending()
                    for (i in indices) FilmDataSource.films.removeAt(i)
                    adapter.notifyDataSetChanged(); mode.finish(); true
                } else false
            }
            override fun onDestroyActionMode(mode: android.view.ActionMode) { seleccionadas.clear() }
        })
    }
}

/* ----------------- Compose UI (sin cambios) ----------------- */
@Composable
fun FilmListScreen(
    onSeeA: () -> Unit,
    onSeeB: () -> Unit,
    onAbout: () -> Unit,
    onExit: () -> Unit
) {
    val blackYellow = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.Yellow)
    Box(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)) {
        Button(onClick = onAbout, colors = blackYellow, modifier = Modifier.align(Alignment.TopEnd)) { Text("Acerca de") }
        Column(Modifier.align(Alignment.Center).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Listado de películas", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))
            Button(onClick = onSeeA, colors = blackYellow, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) { Text("Ver película A") }
            Button(onClick = onSeeB, colors = blackYellow, modifier = Modifier.fillMaxWidth()) { Text("Ver película B") }
        }
        Button(onClick = onExit, colors = blackYellow, modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) { Text("Salir de la aplicación") }
    }
}
