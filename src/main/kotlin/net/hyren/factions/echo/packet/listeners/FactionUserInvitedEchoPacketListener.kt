package net.hyren.factions.echo.packet.listeners

import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.factions.FactionsProvider
import net.hyren.factions.echo.packet.FactionUserInvitedEchoPacket
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class FactionUserInvitedEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: FactionUserInvitedEchoPacket
    ) {
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!
        val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchById(packet.factionId!!)!!

        factionUser.getPlayer()?.sendMessage(
            ComponentBuilder()
                .append("§aVocê foi convidado para entrar na facção §f${faction.fullyQualifiedName}§a.")
                .append("\n")
                .append("§aPara ver sua lista de convites clique ")
                .append("§a§lAQUI")
                .event(
                    ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/f convites"
                    )
                )
                .append("§r§a.")
                .create()
        )
    }

}