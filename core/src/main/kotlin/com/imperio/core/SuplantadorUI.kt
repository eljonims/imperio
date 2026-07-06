/**
 * ARQUITECTURA IMPERIO: Arnés de Pruebas (Stub de Consola).
 * Suplanta temporalmente a la interfaz gráfica de LibGDX para exprimir y testear el Motor de forma asíncrona.
 * Abre un coproceso lector que se destruye limpiamente desde el main evitando hilos zombi (Exit 0).
 */
package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Función principal ejecutable. Suplanta a la UI.
 */
fun main() = runBlocking {
    val motorJuego = Motor()

    println("[SUPLANTADOR] Abriendo el Pipe de escucha del Core...")

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

    // 1. Cargamos las reglas de balanceo globales del Ruleset
    println("[SUPLANTADOR] Solicitando carga de las reglas de la naturaleza...")
    motorJuego.cargarReglasDesdeArchivo()

    // 2. Cargamos las casillas físicas del escenario
    println("[SUPLANTADOR] Solicitando carga del archivo físico 'mapa_inicial.json'...")
    motorJuego.cargarMapaDesdeArchivo("mapa_inicial.json")

    // 3. AUDITORÍA ECONÓMICA DE LAS 7 MONEDAS
    println("\n--- [SUPLANTADOR] AUDITORÍA ECONÓMICA DEL MAPA ---")
    motorJuego.obtenerCasillasEnRAM().forEach { (coordenada, casilla) ->
        val rendimiento = motorJuego.calcularRendimientoCasilla(casilla)
        println("Casilla (${coordenada.first}, ${coordenada.second}) -> Suelo: ${casilla.suelo} | Accidente: ${casilla.accidente}")
        print("   Yield -> Comida: ${rendimiento.comida} | Prod: ${rendimiento.produccion} | Oro: ${rendimiento.oro}")
        println(" | Ciencia: ${rendimiento.ciencia} | Cultura: ${rendimiento.cultura} | Fe: ${rendimiento.fe} | Fel: ${rendimiento.felicidad}")
    }
    println("--------------------------------------------------\n")

    // 4. Simulamos interacciones del usuario
    println("[SUPLANTADOR] El jugador toca la casilla (4, 5)...")
    motorJuego.enviarAccion(AccionJugador.SeleccionarCasilla(4, 5))

    println("[SUPLANTADOR] El jugador decide salir al menú principal...")

    // 5. El apagado controlado
    motorJuego.apagarMotor()
    println("[SUPLANTADOR] Cancelando corrutina de la UI...")
    escuchaUI.cancel()
}
