package net.hyren.factions.faction.land.data

import net.hyren.core.shared.misc.utils.ChatColor
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
data class FactionLand(
    val factionId: EntityID<UUID>,
    val landType: LandType,
    val worldName: String,
    val x: Int,
    val z: Int,
    val createdAt: DateTime,
    var updatedAt: DateTime? = null
) {

    fun isTemporary() = landType == LandType.TEMPORARY

}

enum class LandType(
    val displayName: String,
    val color: ChatColor
) {

    SAFE("Zona protegida", ChatColor.GOLD),
    WAR("Zona de guerra", ChatColor.DARK_RED),
    FREE("Zona livre", ChatColor.GRAY),
    CURRENT("Sua posição", ChatColor.YELLOW),
    ALLY("Terra aliada", ChatColor.AQUA),
    ENEMY("Terra inimiga", ChatColor.RED),
    NEUTRAL("Terra neutra", ChatColor.WHITE),
    CONTESTED("Terra contestada", ChatColor.GRAY),
    TEMPORARY("Terra temporária", ChatColor.BLUE);

}