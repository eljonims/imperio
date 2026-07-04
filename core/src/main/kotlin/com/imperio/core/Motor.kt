package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * La implementación real del cerebro del juego.
 * Aquí dentro vive el estado de la partida y se procesa la lógica.
 */
class Motor : ServiciosMotor {

    // El extremo PRIVADO del escritor: solo el Core puede meter datos aquí
    private val _canalSalida = MutableSharedFlow<AccionCore>()

    // El extremo PÚBLICO del lector: el pipe que ve la UI a través de la interfaz
    override val flujoAccionesCore: Flow<AccionCore> = _canalSalida

    /**
     * La función que la UI llama cuando le da la gana para entregar órdenes.
     */
    override fun enviarAccion(accion: AccionJugador) {
        // Por ahora está vacío, aquí procesaremos los clicks y toques
    }
}
