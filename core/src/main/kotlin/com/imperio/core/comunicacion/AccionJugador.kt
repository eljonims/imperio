package com.imperio.core.comunicacion

sealed interface AccionJugador {
    data class SeleccionarCasilla(val x: Int, val y: Int) : AccionJugador
    data class MoverUnidadA(val destinoX: Int, val destinoY: Int) : AccionJugador
    object TerminarTurno : AccionJugador
}
