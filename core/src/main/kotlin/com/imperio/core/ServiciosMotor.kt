package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.flow.Flow

/**
 * El contrato de servicios principal del cerebro del juego.
 * Define la entrada (UDP-like) y la salida (Río de datos) del Core.
 */
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
