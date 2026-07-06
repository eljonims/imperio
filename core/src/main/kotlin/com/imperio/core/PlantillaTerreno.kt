/**
 * ARQUITECTURA IMPERIO: Plantilla de Configuración Geográfica Data-Driven.
 * Molde inmutable para deserializar las estadísticas físicas y económicas de la naturaleza desde el JSON.
 * Mantiene la separación pura entre la estructura inalterable del código y el balanceo dinámico de los Rulesets.
 */
package com.imperio.core

import kotlinx.serialization.Serializable

/**
 * Contenedor atómico de todas las estadísticas de balanceo de un terreno.
 * Mapea las minúsculas del JSON gracias a kotlinx.serialization.
 */
@Serializable
data class DatosEstadisticas(
    val comida: Int = 0,
    val produccion: Int = 0,
    val oro: Int = 0,
    val ciencia: Int = 0,
    val cultura: Int = 0,
    val fe: Int = 0,
    val felicidad: Int = 0,
    val altura: Int = 0,
    val costeMovimiento: Int = 1,
    val esAcuatico: Boolean = false,
    val oceanoProfundo: Boolean = false,
    val bloqueaMismaAltura: Boolean = false,
    val infranqueableTerrestre: Boolean = false,
    val infranqueableAereo: Boolean = false,
    val terrenosValidos: List<String> = emptyList()
)

/**
 * El contenedor raíz que representa el archivo completo reglas_terreno.json.
 */
@Serializable
data class ReglasTerreno(
    val suelos: Map<String, DatosEstadisticas>,
    val accidentes: Map<String, DatosEstadisticas>
)
