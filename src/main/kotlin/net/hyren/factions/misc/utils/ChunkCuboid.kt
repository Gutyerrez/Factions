package net.hyren.factions.misc.utils

/**
 * @author Gutyerrez
 */
data class ChunkCuboid(
    val minX: Int,
    val minZ: Int,
    val maxX: Int,
    val maxZ: Int
) {

    fun contains(
        x: Int,
        z: Int
    ) = x in minX..maxX && z in minZ..maxZ

}