package net.hyren.factions.listeners.connection

import net.hyren.factions.FactionsProvider
import net.hyren.factions.misc.player.list.*
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Gutyerrez
 */
class PlayerQuitListener : Listener {

    @EventHandler
    fun on(
        event: PlayerQuitEvent
    ) {
        val player = event.player

        FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(player.uniqueId)?.let {
            it.uninitializedPlayerList()
        }

        Bukkit.getOnlinePlayers().forEach {
            FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(it.uniqueId)?.let {
                it.updatePlayerList()
            }
        }
    }

}