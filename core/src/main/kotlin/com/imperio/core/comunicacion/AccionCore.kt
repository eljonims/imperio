package com.imperio.core.comunicacion

sealed interface AccionCore {


    data class ActualizarRuta(val ruta: RutaVisual?) : AccionCore
    data class CambiarTurno(val nuevoTurno: Int) : AccionCore
    data class MostrarMensaje(val texto: String) : AccionCore


    data class RutaVisual(
        val casillasCamino: List<Pair<Int, Int>>,
        val turnosNecesarios: Int
    )
}
