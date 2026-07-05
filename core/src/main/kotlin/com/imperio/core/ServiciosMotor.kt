/**
 * ARQUITECTURA IMPERIO: Interfaz de Frontera entre Módulos.
 * Define el mostrador de servicios aislado que la UI (:ui) consume del cerebro (:core).
 * Entrada asíncrona mediante 'enviarAccion' y salida secuencial (FIFO) a través del Pipe 'flujoAccionesCore' (Flow).
 */

package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.flow.Flow



interface ServiciosMotor {

    /**
     * Canal de SALIDA (El río de datos).
     * La UI se conectará a este flujo para recibir y reaccionar a las
     * actualizaciones del Core (Cambios de turno, mapas, mensajes) a lo largo del tiempo.
     */
    val flujoAccionesCore: Flow<AccionCore>

    /**
     * Canal de ENTRADA.
     * La UI lanza aquí las intenciones del jugador y continúa su ejecución.
     */
    fun enviarAccion(accion: AccionJugador)
}
