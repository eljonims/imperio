/**
 * ARQUITECTURA IMPERIO: Contenedor Inmutable de Rendimientos Económicos y Culturales.
 * Alberga los 7 recursos universales del Core calculados a partir de los Rulesets.
 * Estructura de datos pura utilizada por el Motor para transferir cálculos matemáticos limpios a la UI.
 */
package com.imperio.core.comunicacion

import kotlinx.serialization.Serializable

@Serializable
data class Rendimiento(
    val comida: Int = 0,
    val produccion: Int = 0,
    val oro: Int = 0,
    val ciencia: Int = 0,
    val cultura: Int = 0,
    val fe: Int = 0,
    val felicidad: Int = 0
)
