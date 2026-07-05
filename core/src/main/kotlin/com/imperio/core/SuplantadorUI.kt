/**
 * ARQUITECTURA IMPERIO: Arnés de Pruebas (Stub de Consola).
 * Suplanta temporalmente a la interfaz gráfica de LibGDX para exprimir y testear el Motor de forma asíncrona.
 * Abre un coproceso lector que se destruye limpiamente desde el main evitando hilos zombi (Exit 0).
 */
package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.Job // <--- IMPORTAMOS EL OBJETO DE CONTROL REMOTO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Función principal ejecutable. Suplanta a la UI.
 */
fun main() = runBlocking {
    val motorJuego = Motor()

    println("[SUPLANTADOR] Abriendo el Pipe de escucha del Core...")

    // 1. Guardamos el "control remoto" de la corrutina lectora de la UI en la variable 'escuchaUI'
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

    // 2. EL MAPA EN MINÚSCULAS: Simulamos el texto de un archivo guardado
    val mapaJsonDePrueba = """
        [
          {
            "x": 0,
            "y": 0,
            "suelo": "pradera",
            "accidente": "ninguno",
            "mejora": "granja",
            "estadoRuina": "ninguna",
            "tieneCarretera": true,
            "tieneViaTren": false
          },
          {
            "x": 0,
            "y": 1,
            "suelo": "desierto",
            "accidente": "colina",
            "mejora": "mina",
            "estadoRuina": "disponible",
            "tieneCarretera": false,
            "tieneViaTren": false
          }
        ]
    """.trimIndent()

    // 3. Ordenamos al Motor que devore el JSON y monte la RAM
    println("[SUPLANTADOR] Inyectando el archivo de mapa JSON in situ...")
    motorJuego.inicializarMapaDesdeJson(mapaJsonDePrueba)

    // 4. Simulamos interacciones del usuario
    println("[SUPLANTADOR] El jugador toca la casilla (4, 5)...")
    motorJuego.enviarAccion(AccionJugador.SeleccionarCasilla(4, 5))

    println("[SUPLANTADOR] El jugador decide salir al menú principal...")

    // 5. EL APAGADO EN CADENA PERFECTO Y CONTROLADO:
    // Primero, le ordenamos al Core que apague su 'alcanceMotor' (libera los hilos de fondo)
    motorJuego.apagarMotor()

    // Segundo, destruimos el lector de la UI de forma explícita desde fuera
    println("[SUPLANTADOR] Cancelando corrutina de la UI...")
    escuchaUI.cancel()
}
