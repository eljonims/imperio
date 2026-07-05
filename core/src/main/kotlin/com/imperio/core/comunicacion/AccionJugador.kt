package com.imperio.core.comunicacion


sealed interface AccionJugador {

    data class SeleccionarCasilla(val x: Int, val y: Int) : AccionJugador
    data class MoverA(val x: Int, val y: Int) : AccionJugador
    object TerminarTurno : AccionJugador
    object SalirAlMenuPrincipal : AccionJugador
}
