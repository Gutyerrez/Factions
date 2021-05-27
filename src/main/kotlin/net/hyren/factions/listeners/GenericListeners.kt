package net.hyren.factions.listeners

import net.hyren.factions.alpha.misc.player.list.data.PlayerList
import org.bukkit.entity.Player
import org.bukkit.event.*
import org.bukkit.event.entity.EntitySpawnEvent

/**
 * @author Gutyerrez
 */
class GenericListeners : Listener {

    @EventHandler
    fun on(
        event: EntitySpawnEvent
    ) {
        val entity = event.entity

        if (entity is Player) {
            PlayerList.hideCommonPlayers(entity)
        }
    }

}