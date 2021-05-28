package net.hyren.factions.listeners.connection

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.groups.Group
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
        val player = event.player
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(player.uniqueId)!!
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user.id)

        Bukkit.getOnlinePlayers().forEach {
            FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(it.uniqueId)?.let {
                when {
                    user.hasGroup(Group.HELPER) && !it.hasFaction() -> it.updatePlayerList()
                    it.factionId != null && it.factionId == factionUser?.factionId -> it.updatedAt
                    else -> Unit
                }
            }
        }

        factionUser?.updatePlayerList()
    }

}