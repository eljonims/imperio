/**
 * ARQUITECTURA IMPERIO: Arnés de Pruebas (Stub de Consola).
 * Suplanta temporalmente a la interfaz gráfica de LibGDX para exprimir y testear el Motor de forma asíncrona.
 * Abre un coproceso lector que se destruye limpiamente desde el main evitando hilos zombi (Exit 0).
 */
package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Función principal ejecutable. Suplanta a la UI.
 */
fun main() = runBlocking {
    val motorJuego = Motor()

    println("[SUPLANTADOR] Abriendo el Pipe de escucha del Core...")

    // 1. Guardamos el "control remoto" de la corrutina lectora de la UI
    val escuchaUI: Job = launch {
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
                }
            }
        }
    }

    // 2. EL SALTO DE FE: Le ordenamos al Motor leer el archivo REAL del disco duro
    println("[SUPLANTADOR] Solicitando carga del archivo físico 'mapa_inicial.json'...")
    motorJuego.cargarMapaDesdeArchivo("mapa_inicial.json")

    // 3. Simulamos interacciones del usuario
    println("[SUPLANTADOR] El jugador toca la casilla (4, 5)...")
    motorJuego.enviarAccion(AccionJugador.SeleccionarCasilla(4, 5))

    println("[SUPLANTADOR] El jugador decide salir al menú principal...")

    // 4. El apagado en cadena controlado libre de hilos zombi (Exit 0)
    motorJuego.apagarMotor()
    println("[SUPLANTADOR] Cancelando corrutina de la UI...")
    escuchaUI.cancel()
}
