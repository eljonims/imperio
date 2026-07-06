/**
 * ARQUITECTURA IMPERIO: Representación Inmutable de la Geografía del Juego.
 * Define las celdas físicas del mapa bidimensional mediante tipos fuertemente tipados.
 * Aplica el principio de inmutabilidad (data class con val) usando .copy() para la progresión del mapa.
 * Los rendimientos reales (comida, producción, oro) no son fijos, se calculan dinámicamente inyectando la lista de Efectos.
 */
package com.imperio.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * CONTENEDOR GEOGRÁFICO: Agrupa los listados cerrados del mapa por capas físicas
 * para evitar que queden dispersos por el proyecto.
 */
interface Terreno {

    // Capa 1: El suelo base fundamental de la celda (Catálogo real de Unciv)
    @Serializable
    enum class Suelo {
        @SerialName("pradera") PRADERA,
        @SerialName("desierto") DESIERTO,
        @SerialName("sabana") SABANA,
        @SerialName("tundra") TUNDRA,
        @SerialName("nieve") NIEVE,
        @SerialName("montana") MONTANA,
        @SerialName("costa") COSTA,
        @SerialName("oceano") OCEANO,
        @SerialName("lago") LAGO
    }

    // Capa 2: Accidentes geográficos, aspectos y Maravillas Naturales exclusivas
    @Serializable
    enum class Accidente {
        @SerialName("ninguno") NINGUNO,

        // Aspectos y Accidentes Estándar
        @SerialName("colina") COLINA,
        @SerialName("bosque") BOSQUE,
        @SerialName("selva") SELVA,
        @SerialName("pantano") PANTANO,
        @SerialName("cienaga") CIENAGA,
        @SerialName("atolon") ATOLON,
        @SerialName("hielo") HIELO,
        @SerialName("oasis") OASIS,
        @SerialName("rio") RIO,
        @SerialName("terreno_radioactivo") TERRENO_RADIOACTIVO,

        // Maravillas Naturales (Comparten la misma capa excluyente)
        @SerialName("maravilla_potosi") MARAVILLA_POTOSI,
        @SerialName("maravilla_barringer") MARAVILLA_BARRINGER,
        @SerialName("maravilla_dorado") MARAVILLA_DORADO,
        @SerialName("maravilla_juventud") MARAVILLA_JUVENTUD,
        @SerialName("maravilla_coral") MARAVILLA_CORAL,
        @SerialName("maravilla_meseta") MARAVILLA_MESETA,
        @SerialName("maravilla_krakatoa") MARAVILLA_KRAKATOA,
        @SerialName("maravilla_fuji") MARAVILLA_FUJI,
        @SerialName("maravilla_kailash") MARAVILLA_KAILASH,
        @SerialName("maravilla_sinai") MARAVILLA_SINAI,
        @SerialName("maravilla_gibraltar") MARAVILLA_GIBRALTAR,
        @SerialName("maravilla_adan") MARAVILLA_ADAN

    }

    // Capa 3: La infraestructura artificial construida por los trabajadores del jugador
    @Serializable
    enum class Mejora {
        @SerialName("ninguna") NINGUNA,
        @SerialName("granja") GRANJA,
        @SerialName("mina") MINA,
        @SerialName("campamento") CAMPAMENTO,
        @SerialName("puesto_comercial") PUESTO_COMERCIAL,
        @SerialName("academia") ACADEMIA
    }

    // Capa Extra: El estado del patrimonio arqueológico
    @Serializable
    enum class Ruina {
        @SerialName("ninguna") NINGUNA,
        @SerialName("disponible") DISPONIBLE,
        @SerialName("explorando") EXPLORANDO,
        @SerialName("saqueada") SAQUEADA
    }
}

/**
 * LA CELDA PURA: Estructura inmutable que representa el sándwich de 4 capas y la red de transporte del mapa.
 */
@Serializable
data class Casilla(
    val x: Int,
    val y: Int,
    val suelo: Terreno.Suelo,
    val accidente: Terreno.Accidente = Terreno.Accidente.NINGUNO,
    val mejora: Terreno.Mejora = Terreno.Mejora.NINGUNA,
    val estadoRuina: Terreno.Ruina = Terreno.Ruina.NINGUNA,

    // Red de transporte independiente booleanas
    val tieneCarretera: Boolean = false,
    val tieneViaTren: Boolean = false
)
