package com.imperio.core.comunicacion

/**
 * LA FOTO DEL JUEGO: Estructura fija y real que representa el estado de la partida
 * en un instante concreto. La UI leerá esto para dibujarlo todo.
 */
data class EstadoJuego(
    val turnoActual: Int = 1,
    val mensajePantalla: String? = null,
    val coordenadaSeleccionada: Pair<Int, Int>? = null,
    val rutaSimulada: RutaVisual? = null
) {

    data class RutaVisual(
        val casillasCamino: List<Pair<Int, Int>>,
        val turnosNecesarios: Int
    )
}
