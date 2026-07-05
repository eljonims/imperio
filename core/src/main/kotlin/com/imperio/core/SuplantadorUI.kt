package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Función principal ejecutable (Arnés de pruebas definitivo).
 * Suplanta a la UI para exprimir la lógica del Motor por consola.
 */
fun main() = runBlocking {
    val motorJuego: ServiciosMotor = Motor()

    println("[SUPLANTADOR] Abriendo el Pipe de escucha del Core...")

    // 1. El Hilo de la UI abre la tubería infinita de lectura (collect)
    launch {
        motorJuego.flujoAccionesCore.collect { accionCore ->
            // Usamos un 'when' básico para desglosar lo que nos tira el Core
            when (accionCore) {
                is AccionCore.NuevoEstado -> {
                    val foto = accionCore.estado
                    println("\n--- [SUPLANTADOR] ¡NUEVA FOTO DEL JUEGO DETECTADA! ---")
                    println("-> Turno Actual: ${foto.turnoActual}")
                    println("-> Mensaje en Pantalla: ${foto.mensajePantalla}")
                    println("-> Casilla Seleccionada: ${foto.coordenadaSeleccionada}")
                    println("-> Ruta en Pantalla (Flechas): ${foto.rutaSimulada?.casillasCamino}")
                    println("----------------------------------------------------\n")
                }
                is AccionCore.MostrarMensaje -> {
                    println("[SUPLANTADOR] Mensaje flotante: ${accionCore.texto}")
                }
            }
        }
    }

    // 2. Simulamos la vida asíncrona: El jugador interactúa cuando le da la gana
    println("[SUPLANTADOR] El jugador toca el botón de Pasar Turno...")
    motorJuego.enviarAccion(AccionJugador.TerminarTurno)

    println("[SUPLANTADOR] El jugador toca la casilla lejana (4, 5)...")
    motorJuego.enviarAccion(AccionJugador.SeleccionarCasilla(4, 5))
}
