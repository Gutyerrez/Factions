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

    ALLY("Aliada", ChatColor.AQUA),
    NEUTRAL("Neutra", ChatColor.WHITE),
    ENEMY("Inimiga", ChatColor.RED),
    CURRENT("Sua posição", ChatColor.YELLOW),
    FREE("Zona Livre", ChatColor.GRAY),
    SAFE("Zona Protegida", ChatColor.GOLD),
    WAR("Zona de Guerra", ChatColor.DARK_RED),
    CONTESTED("???", ChatColor.GRAY),
    UNDER_ATTACK("Sob ataque", ChatColor.LIGHT_PURPLE),
    TEMPORARY("Terrenos temporários", ChatColor.BLUE);

}