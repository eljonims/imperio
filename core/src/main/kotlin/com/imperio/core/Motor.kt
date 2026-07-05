/**
 * ARQUITECTURA IMPERIO: Implementación del Motor Lógico Central.
 * Guarda en memoria RAM privada ('private var') el estado mutable del juego y procesa la partida en un hilo de fondo.
 * Reacciona al when de tipos (is), calcula consecuencias con corrutinas y emite copias (.copy) inmutables por el canal seguro.
 */

package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch



class Motor : ServiciosMotor {

    // El "ámbito" o motor de hilos que nos permite lanzar tareas en segundo plano de forma segura
    private val alcanceMotor = CoroutineScope(Dispatchers.Default)

    private val _canalSalida = MutableSharedFlow<AccionCore>(replay = 1)
    override val flujoAccionesCore: Flow<AccionCore> = _canalSalida

    /**
     * La función que la UI llama cuando le da la gana para entregar órdenes.
     */
    override fun enviarAccion(accion: AccionJugador) {
        // Usamos el 'when' simétrico para inspeccionar qué paquete nos ha llegado
        when (accion) {
            is AccionJugador.TerminarTurno -> {
                // Lanzamos un subproceso asíncrono seguro
                alcanceMotor.launch {
                    // Metemos el mensaje de respuesta en el Pipe
                    _canalSalida.emit(AccionCore.MostrarMensaje("El turno ha avanzado con éxito"))
                }
            }
            is AccionJugador.SeleccionarCasilla -> {
                // Por ahora lo dejamos vacío para el siguiente paso
            }
            is AccionJugador.MoverA -> {
                // Por ahora lo dejamos vacío para el siguiente paso
            }
            is AccionJugador.SalirAlMenuPrincipal -> {
                alcanceMotor.launch {
                    println("[CORE] El jugador ha solicitado salir. Despachando mensaje de cierre...")
                    // Enviamos un último mensaje de cortesía por la tubería
                    _canalSalida.emit(AccionCore.MostrarMensaje("Partida finalizada. Cerrando conexiones."))
                }
            }
        }
    }
}
