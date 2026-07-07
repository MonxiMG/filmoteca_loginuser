package es.ua.eps.filmoteca

object FilmDataSource {

    // Lista mutable de películas (SE MUTA, NO SE REASIGNA)
    val films: MutableList<Film> = mutableListOf(
        Film(
            title = "The Matrix",
            director = "Lana & Lilly Wachowski",
            year = 1999,
            genre = "Sci-Fi",
            format = "Blu-ray",
            imdbUrl = "https://www.imdb.com/title/tt0133093/",
            posterRes = R.drawable.ic_launcher_foreground, // pon tu drawable/placeholder
            notes = "Clásico de ciencia ficción"
        ),
        Film(
            title = "Inception",
            director = "Christopher Nolan",
            year = 2010,
            genre = "Sci-Fi",
            format = "Digital",
            imdbUrl = "https://www.imdb.com/title/tt1375666/",
            posterRes = R.drawable.ic_launcher_foreground,
            notes = ""
        ),
        Film(
            title = "Spirited Away",
            director = "Hayao Miyazaki",
            year = 2001,
            genre = "Animación",
            format = "DVD",
            imdbUrl = "https://www.imdb.com/title/tt0245429/",
            posterRes = R.drawable.ic_launcher_foreground,
            notes = "Studio Ghibli"
        )
    )

    // Utilidades (por si las necesitas)
    fun add(film: Film) {
        films.add(film)
    }

    fun removeAt(index: Int) {
        if (index in films.indices) films.removeAt(index)
    }

    fun clearAll() {
        films.clear() // OJO: esto muta la lista; no reasigna 'films'
    }
}
