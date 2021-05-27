package net.hyren.factions.echo.packet.listeners

import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.factions.FactionsProvider
import net.hyren.factions.echo.packet.FactionUserUpdatedEchoPacket
import net.hyren.factions.misc.tab.list.updatePlayerList
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class FactionUserUpdatedEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: FactionUserUpdatedEchoPacket
    ) {
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)!!

        FactionsProvider.Cache.Local.FACTION_USER.provide().refresh(factionUser)

        FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(packet.factionUserId!!)?.let {
            it.updatePlayerList()
        }
    }

}