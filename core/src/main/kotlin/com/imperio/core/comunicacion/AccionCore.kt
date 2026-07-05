package com.imperio.core.comunicacion


sealed interface AccionCore {
    data class NuevoEstado(val estado: EstadoJuego) : AccionCore
    data class MostrarMensaje(val texto: String) : AccionCore
}
