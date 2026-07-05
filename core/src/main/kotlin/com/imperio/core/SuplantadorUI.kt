/**
 * ARQUITECTURA IMPERIO: Arnés de Pruebas (Stub de Consola).
 * Suplanta temporalmente a la interfaz gráfica de LibGDX para exprimir y testear el Motor de forma asíncrona.
 * Abre un coproceso lector que se autocierra limpiamente mediante Context[Job]?.cancel() evitando hilos zombi (Exit 0).
 */

package com.imperio.core

import com.imperio.core.comunicacion.AccionCore
import com.imperio.core.comunicacion.AccionJugador
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking




fun main() = runBlocking {
    val motorJuego: ServiciosMotor = Motor()

    println("[SUPLANTADOR] Abriendo el Pipe de escucha del Core...")


    launch {
        motorJuego.flujoAccionesCore.collect { accionCore ->
            when (accionCore) {
                is AccionCore.NuevoEstado -> {
                    val foto = accionCore.estado
                    println("\n--- [SUPLANTADOR] NUEVO ESTADO DETECTADO ---")
                    println("-> Turno Actual: ${foto.turnoActual}")
                    println("--------------------------------------------\n")
                }
                is AccionCore.MostrarMensaje -> {
                    println("[SUPLANTADOR] Mensaje recibido: ${accionCore.texto}")

                    // AUTOCIERRE: Si el mensaje contiene la orden de cierre, nos destruimos
                    if (accionCore.texto.contains("Cerrando")) {
                        println("[SUPLANTADOR] Detectada orden de fin. Autocancelando coproceso...")
                        this.coroutineContext[Job]?.cancel()
                    }
                }
            }
        }
    }

    println("[SUPLANTADOR] El jugador toca la casilla (4, 5)...")
    motorJuego.enviarAccion(AccionJugador.SeleccionarCasilla(4, 5))

    println("[SUPLANTADOR] El jugador decide salir al menú principal...")
    motorJuego.enviarAccion(AccionJugador.SalirAlMenuPrincipal)
}
