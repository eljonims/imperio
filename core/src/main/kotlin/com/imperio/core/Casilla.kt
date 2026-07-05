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

    // Capa 1: El suelo base fundamental de la celda (Nunca cambia)
    @Serializable
    enum class Suelo {
        @SerialName("pradera") PRADERA,
        @SerialName("desierto") DESIERTO,
        @SerialName("tundra") TUNDRA,
        @SerialName("nieve") NIEVE,
        @SerialName("costa") COSTA,
        @SerialName("oceano") OCEANO
    }

    // Capa 2: El accidente o característica geográfica natural superpuesta al suelo
    @Serializable
    enum class Accidente {
        @SerialName("ninguno") NINGUNO,
        @SerialName("colina") COLINA,
        @SerialName("montana") MONTANA,
        @SerialName("bosque") BOSQUE,
        @SerialName("selva") SELVA,
        @SerialName("pantano") PANTANO
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
 * Las coordenadas son fijas, las capas usan 'Terreno' y las rutas de transporte se manejan mediante booleanos eficientes.
 */
@Serializable
data class Casilla(
    val x: Int,
    val y: Int,
    val suelo: Terreno.Suelo,
    val accidente: Terreno.Accidente = Terreno.Accidente.NINGUNO,
    val mejora: Terreno.Mejora = Terreno.Mejora.NINGUNA,
    val estadoRuina: Terreno.Ruina = Terreno.Ruina.NINGUNA,

    // Red de transporte independiente (Convive con las mejoras)
    val tieneCarretera: Boolean = false,
    val tieneViaTren: Boolean = false
)
