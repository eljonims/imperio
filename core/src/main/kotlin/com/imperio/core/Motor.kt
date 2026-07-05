/**
 * ARQUITECTURA IMPERIO: Implementación del Motor Lógico Central.
 * Guarda en memoria RAM privada ('private var') el estado mutable del juego y procesa la partida en un hilo de fondo.
 * Reacciona al when de tipos (is), calcula consecuencias con corrutinas y emite copias (.copy) inmutables por el canal seguro.
 */
package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import com.imperio.core.comunicacion.EstadoJuego
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel // <--- EL IMPORT QUE FALTABA: Activa la extensión .cancel() del Scope
import kotlinx.serialization.json.Json

/**
 * La implementación real y definitiva del cerebro del juego.
 */
class Motor : ServiciosMotor {

    private val alcanceMotor = CoroutineScope(Dispatchers.Default)
    private val _canalSalida = MutableSharedFlow<AccionCore>(replay = 1)
    override val flujoAccionesCore: Flow<AccionCore> = _canalSalida

    private var estadoActual = EstadoJuego()
    private var mapaCasillas: Map<Pair<Int, Int>, Casilla> = emptyMap()

    fun inicializarMapaDesdeJson(jsonTexto: String) {
        try {
            val listaCasillas: List<Casilla> = Json.decodeFromString(jsonTexto)
            mapaCasillas = listaCasillas.associateBy { casilla -> Pair(casilla.x, casilla.y) }
            println("[CORE] Mapa cargado con éxito. Total de casillas en RAM: ${mapaCasillas.size}")
        } catch (e: Exception) {
            println("[CORE] ERROR CRÍTICO al deserializar el mapa JSON: ${e.message}")
        }
    }

    override fun apagarMotor() {
        println("[CORE] Solicitando cierre. Despachando mensaje final...")
        // 1. Emitimos el mensaje de despedida de forma instantánea y segura
        _canalSalida.tryEmit(AccionCore.MostrarMensaje("Partida finalizada. Cerrando conexiones."))

        // 2. Ejecutamos el tiro de gracia: matamos el Scope y liberamos los hilos nativos de la CPU
        println("[CORE] Cancelando alcanceMotor y liberando hilos de la CPU...")
        alcanceMotor.cancel()
    }

    override fun enviarAccion(accion: AccionJugador) {
        when (accion) {
            is AccionJugador.TerminarTurno -> {
                alcanceMotor.launch {
                    estadoActual = estadoActual.copy(
                        turnoActual = estadoActual.turnoActual + 1,
                        mensajePantalla = "El turno ha avanzado con éxito"
                    )
                    _canalSalida.emit(AccionCore.NuevoEstado(estadoActual))
                }
            }
            is AccionJugador.SeleccionarCasilla -> {
                alcanceMotor.launch {
                    val caminoFalso = listOf(Pair(0, 0), Pair(1, 1), Pair(accion.x, accion.y))
                    val rutaCalculada = EstadoJuego.RutaVisual(caminoFalso, turnosNecesarios = 2)

                    estadoActual = estadoActual.copy(
                        coordenadaSeleccionada = Pair(accion.x, accion.y),
                        rutaSimulada = rutaCalculada,
                        mensajePantalla = "Casilla seleccionada: (${accion.x}, ${accion.y})"
                    )
                    _canalSalida.emit(AccionCore.NuevoEstado(estadoActual))
                }
            }
            is AccionJugador.MoverA -> {
                // Se completará cuando definamos las unidades en el mapa
            }
            is AccionJugador.SalirAlMenuPrincipal -> {
                // El caso se queda vacío porque la UI llamará directamente a apagarMotor()
            }
        }
    }
}
