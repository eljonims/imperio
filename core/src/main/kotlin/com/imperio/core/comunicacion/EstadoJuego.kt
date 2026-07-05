/**
 * ARQUITECTURA IMPERIO: Contrato de Datos Centralizado.
 * Representa la fotografía fija, inmutable y de solo lectura de la partida en un instante.
 * Se transfiere a la UI mediante referencias eficientes de la JVM (punteros ocultos) para evitar duplicación en RAM.
 */

package com.imperio.core.comunicacion



data class EstadoJuego(
    val turnoActual: Int = 1,
    val mensajePantalla: String? = null,
    val coordenadaSeleccionada: Pair<Int, Int>? = null,
    val rutaSimulada: RutaVisual? = null
) {

    data class RutaVisual(
        val casillasCamino: List<Pair<Int, Int>>,
        val turnosNecesarios: Int
    )
}
