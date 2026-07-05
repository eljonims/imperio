package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Función principal ejecutable. Actúa como un jugador falso (Suplantador de UI)
 * para probar el motor por consola sin encender gráficos.
 */
fun main() = runBlocking {
    // 1. Instanciamos el mostrador de servicios y el operario real
    val motorJuego: ServiciosMotor = Motor()

    println("[SUPLANTADOR] Conectando la pantalla al canal de salida del Core...")

    // 2. Abrimos la tubería (Pipe) en un subproceso para LEER siempre en bucle
    launch {
        motorJuego.flujoAccionesCore.collect { respuesta ->
            println("[SUPLANTADOR] ¡He recibido un mensaje del Core! -> $respuesta")
        }
    }

    println("[SUPLANTADOR] Enviando acción ficticia: TerminarTurno...")

    // 3. La UI envía una acción cuando le da la gana
    motorJuego.enviarAccion(AccionJugador.TerminarTurno)
}
