package net.hyren.factions.listeners.connection

import net.hyren.factions.FactionsProvider
import net.hyren.factions.misc.player.list.updatePlayerList
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author Gutyerrez
 */
class PlayerJoinListener : Listener {

    @EventHandler
    fun on(
        event: PlayerJoinEvent
    ) {
//        val player = event.player
//
//        PlayerList.hideCommonPlayers(player)
//
        Bukkit.getOnlinePlayers().forEach {
            FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(it.uniqueId)?.let {
                it.updatePlayerList()
            }
        }
    }

}