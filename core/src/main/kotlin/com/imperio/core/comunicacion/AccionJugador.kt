/**
 * ARQUITECTURA IMPERIO: Contrato de Entrada (UI -> CORE).
 * Catálogo cerrado (Sealed Interface) de intenciones inmutables del usuario.
 * Flujo asíncrono estilo UDP (dispara y olvida). Las coordenadas usan data class; los botones puros usan object.
 */

package com.imperio.core.comunicacion




sealed interface AccionJugador {

    data class SeleccionarCasilla(val x: Int, val y: Int) : AccionJugador
    data class MoverA(val x: Int, val y: Int) : AccionJugador
    object TerminarTurno : AccionJugador
    object SalirAlMenuPrincipal : AccionJugador
}
