package net.hyren.factions.echo.packet.listeners

import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.factions.FactionsProvider
import net.hyren.factions.echo.packet.FactionUserInviteAcceptedEchoPacket
import net.md_5.bungee.api.chat.TextComponent
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class FactionUserInviteAcceptedEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: FactionUserInviteAcceptedEchoPacket
    ) {
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!

        FactionsProvider.Cache.Local.FACTION_USER.provide().refresh(factionUser)
        FactionsProvider.Cache.Local.FACTION_INVITES.provide().refresh(factionUser)

        val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchById(packet.factionId!!)!!

        FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)?.let {
            faction.getOnlinePlayers()
                .filter { player -> player.uniqueId != it.getUniqueId() }
                .forEach { player ->
                    player.sendMessage(
                        TextComponent("§aO usuário ${it.getFancyName()} §aé o mais novo membro da facção.")
                    )
                }
        }
    }

}