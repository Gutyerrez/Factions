package net.hyren.factions.listeners.connection

import net.hyren.core.shared.CoreProvider
import net.hyren.factions.FactionsProvider
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author Gutyerrez
 */
class PlayerJoinListener : Listener {

    @EventHandler
    fun on(
        event: PlayerJoinEvent
    ) {
        val player = event.player

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(player.uniqueId)!!
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user.id) ?: return

        factionUser.initPlayerList(player)
    }

}