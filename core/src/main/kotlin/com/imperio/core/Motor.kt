/**
 * ARQUITECTURA IMPERIO: Implementación del Motor Lógico Central.
 * Guarda en memoria RAM privada ('private var') el estado mutable del juego y procesa la partida en un hilo de fondo.
 * Reacciona al when de tipos (is), calcula consecuencias con corrutinas y emite copias (.copy) inmutables por el canal seguro.
 */
package com.imperio.core

import com.imperio.core.comunicacion.AccionJugador
import com.imperio.core.comunicacion.AccionCore
import com.imperio.core.comunicacion.EstadoJuego
import com.imperio.core.comunicacion.Rendimiento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import kotlinx.serialization.json.Json

class Motor : ServiciosMotor {

    private val alcanceMotor = CoroutineScope(Dispatchers.Default)
    private val _canalSalida = MutableSharedFlow<AccionCore>(replay = 1)
    override val flujoAccionesCore: Flow<AccionCore> = _canalSalida

    private var estadoActual = EstadoJuego()
    private var mapaCasillas: Map<Pair<Int, Int>, Casilla> = emptyMap()

    // LA BIBLIA DEL BALANCEO: Guarda las estadísticas cargadas desde reglas_terreno.json
    private var reglasMundo: ReglasTerreno? = null

    private val rulesetActivo = "rulesets/civ5_base"

    /**
     * Carga las estadísticas de rendimiento, altura y movilidad de la naturaleza desde el JSON.
     */
    fun cargarReglasDesdeArchivo() {
        try {
            val rutaCompleta = "$rulesetActivo/reglas_terreno.json"
            val stream = this::class.java.classLoader.getResourceAsStream(rutaCompleta)
                ?: throw IllegalArgumentException("No se encontró el archivo '$rutaCompleta'")

            val textoJson = stream.bufferedReader().use { it.readText() }

            // Absorber el JSON usando el molde inmutable de PlantillaTerreno.kt
            reglasMundo = Json.decodeFromString<ReglasTerreno>(textoJson)

            println("[CORE] Reglas de la naturaleza cargadas con éxito desde '$rulesetActivo'.")
        } catch (e: Exception) {
            println("[CORE] ERROR CRÍTICO al cargar las reglas del terreno: ${e.message}")
        }
    }

    fun inicializarMapaDesdeJson(jsonTexto: String) {
        try {
            val listaCasillas: List<Casilla> = Json.decodeFromString(jsonTexto)
            mapaCasillas = listaCasillas.associateBy { casilla -> Pair(casilla.x, casilla.y) }
            println("[CORE] Mapa cargado con éxito. Total de casillas en RAM: ${mapaCasillas.size}")
        } catch (e: Exception) {
            println("[CORE] ERROR CRÍTICO al deserializar el mapa JSON: ${e.message}")
        }
    }

    fun cargarMapaDesdeArchivo(nombreArchivo: String) {
        try {
            val rutaCompleta = "$rulesetActivo/$nombreArchivo"
            val stream = this::class.java.classLoader.getResourceAsStream(rutaCompleta)
                ?: throw IllegalArgumentException("No se encontró el archivo '$rutaCompleta'")

            val textoJson = stream.bufferedReader().use { it.readText() }
            println("[CORE] Archivo '$nombreArchivo' leído con éxito desde el ruleset '$rulesetActivo'.")
            inicializarMapaDesdeJson(textoJson)
        } catch (e: Exception) {
            println("[CORE] ERROR CRÍTICO al leer el archivo físico del mapa: ${e.message}")
        }
    }

    /**
     * MATEMÁTICAS DATA-DRIVEN: Calcula el rendimiento real de una casilla en la RAM.
     * Suma de forma atómica las 7 estadísticas universales cruzando el Suelo y el Accidente.
     */
    fun calcularRendimientoCasilla(casilla: Casilla): Rendimiento {
        val reglas = reglasMundo ?: return Rendimiento()

        val statsSuelo = reglas.suelos[casilla.suelo.name.lowercase()]
        val statsAccidente = reglas.accidentes[casilla.accidente.name.lowercase()]

        var comidaFinal = statsSuelo?.comida ?: 0
        var produccionFinal = statsSuelo?.produccion ?: 0
        var oroFinal = statsSuelo?.oro ?: 0
        var cienciaFinal = statsSuelo?.ciencia ?: 0
        var culturaFinal = statsSuelo?.cultura ?: 0
        var feFinal = statsSuelo?.fe ?: 0
        var felicidadFinal = statsSuelo?.felicidad ?: 0

        if (statsAccidente != null) {
            comidaFinal += statsAccidente.comida
            produccionFinal += statsAccidente.produccion
            oroFinal += statsAccidente.oro
            cienciaFinal += statsAccidente.ciencia
            culturaFinal += statsAccidente.cultura
            feFinal += statsAccidente.fe
            felicidadFinal += statsAccidente.felicidad
        }

        return Rendimiento(
            comida = comidaFinal,
            produccion = produccionFinal,
            oro = oroFinal,
            ciencia = cienciaFinal,
            cultura = culturaFinal,
            fe = feFinal,
            felicidad = felicidadFinal
        )
    }

    fun obtenerCasillasEnRAM(): Map<Pair<Int, Int>, Casilla> = mapaCasillas

    override fun apagarMotor() {
        println("[CORE] Solicitando cierre. Despachando mensaje final...")
        _canalSalida.tryEmit(AccionCore.MostrarMensaje("Partida finalizada. Cerrando conexiones."))
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
            is AccionJugador.MoverA -> {}
            is AccionJugador.SalirAlMenuPrincipal -> {}
        }
    }
}
