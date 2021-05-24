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
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!
        val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchById(packet.factionId!!)!!

        FactionsProvider.Cache.Local.FACTION_USER.provide().refresh(factionUser)
        FactionsProvider.Cache.Local.FACTION_INVITES.provide().refresh(factionUser)

        factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!

        faction.getOnlinePlayers().filter { it.uniqueId != factionUser.getUniqueId() }.forEach {
            it.sendMessage(
                TextComponent("§aO usuário ${factionUser.getFancyName()} §aé o mais novo membro da facção.")
            )
        }
    }

}