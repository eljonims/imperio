/**
 * ARQUITECTURA IMPERIO: Sistema Atómico de Modificadores de Reglas (Efectos).
 * Supera la verbosidad y lentitud de Unciv eliminando el parseo de Strings y Regex.
 * Las habilidades se desglosan en Estructuras de Datos Puras bajo tres pilares: Ámbito, Filtro y Efecto.
 * Los cálculos matemáticos se unifican bajo la fórmula lineal uniforme: (Original * Multiplicador) + Suma.
 * La lógica booleana compleja se resuelve mediante:
 *  - OR: Registros (filas) independientes en el catálogo.
 *  - AND / NOT Múltiples: Listas de criterios tipados con banderas de negación en una única fila.
 */
package com.imperio.core.comunicacion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * CONTENEDOR DE DICCIONARIOS: Agrupa los listados cerrados del sistema de reglas
 * para evitar que queden dispersos por el proyecto.
 */
interface Modificador {

    @Serializable
    enum class Id {
        @SerialName("comida") COMIDA,
        @SerialName("produccion") PRODUCCION,
        @SerialName("oro") ORO,
        @SerialName("ciencia") CIENCIA,
        @SerialName("cultura") CULTURA,
        @SerialName("fuerza_combate") FUERZA_COMBATE,
        @SerialName("puntos_movimiento") PUNTOS_MOVIMIENTO,
        @SerialName("rango_vision") RANGO_VISION,
        @SerialName("coste_mantenimiento") COSTE_MANTENIMIENTO
    }

    @Serializable
    enum class Destino {
        @SerialName("casilla") CASILLA,
        @SerialName("ciudad") CIUDAD,
        @SerialName("unidad") UNIDAD,
        @SerialName("global") GLOBAL
    }

    @Serializable
    enum class Contexto {
        @SerialName("cualquiera") CUALQUIERA,
        @SerialName("cuerpo_a_cuerpo") CUERPO_A_CUERPO,
        @SerialName("distancia") DISTANCIA,
        @SerialName("civil") CIVIL,
        @SerialName("tierra") TIERRA,
        @SerialName("agua") AGUA,
        @SerialName("colina") COLINA,
        @SerialName("rio") RIO,
        @SerialName("unidad_terrestre") UNIDAD_TERRESTRE,
        @SerialName("unidad_acuatica") UNIDAD_ACUATICA,
        @SerialName("unidad_aerea") UNIDAD_AEREA
    }
}

/**
 * Catálogo cerrado de condiciones lógicas individuales.
 */
@Serializable
sealed interface Criterio {
    val esNegacion: Boolean

    @Serializable
    @SerialName("DeContexto")
    data class DeContexto(
        val tipo: Modificador.Contexto,
        override val esNegacion: Boolean = false
    ) : Criterio

    @Serializable
    @SerialName("DeEstado")
    data class DeEstado(
        val siEstaHerido: Boolean = false,
        val siAtaca: Boolean = false,
        val siDefiende: Boolean = false,
        override val esNegacion: Boolean = false
    ) : Criterio
}

/**
 * LA REGLA PURA: Estructura inmutable que define una habilidad o bonus.
 * Usa los tipos organizados dentro de la interfaz 'Modificador'.
 */
@Serializable
data class Efecto(
    val id: Modificador.Id,
    val destino: Modificador.Destino,
    val multiplicador: Float = 1.0f,
    val sumador: Int = 0,
    val criteriosObligatorios: List<Criterio> = emptyList()
)
