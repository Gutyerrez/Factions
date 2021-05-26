package net.hyren.factions.echo.packet.listeners

import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.factions.FactionsProvider
import net.hyren.factions.echo.packet.FactionUserUpdateEchoPacket
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class FactionUserUpdateEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: FactionUserUpdateEchoPacket
    ) {
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!

        FactionsProvider.Cache.Local.FACTION_USER.provide().refresh(factionUser)

        factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!

        factionUser.updatePlayerList()
    }

}