package net.hyren.factions.echo.packet

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FactionUserInviteAcceptedEchoPacket(
    var factionUserId: EntityID<UUID>? = null,
    var factionId: EntityID<UUID>? = null
) : EchoPacket() {

    override fun write(
        buffer: EchoBufferOutput
    ) {
        buffer.writeEntityID(factionUserId)
        buffer.writeEntityID(factionId)
    }

    override fun read(
        buffer: EchoBufferInput
    ) {
        factionUserId = buffer.readEntityID()
        factionId = buffer.readEntityID()
    }

}