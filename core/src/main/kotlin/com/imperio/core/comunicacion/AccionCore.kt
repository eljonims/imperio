/**
 * ARQUITECTURA IMPERIO: Contrato de Salida (CORE -> UI).
 * Catálogo cerrado (Sealed Interface) simétrico a AccionJugador.
 * Transporta el EstadoJuego centralizado o alertas textuales directas que la UI consumirá de forma pasiva.
 */

package com.imperio.core.comunicacion




sealed interface AccionCore {
    data class NuevoEstado(val estado: EstadoJuego) : AccionCore
    data class MostrarMensaje(val texto: String) : AccionCore
}
