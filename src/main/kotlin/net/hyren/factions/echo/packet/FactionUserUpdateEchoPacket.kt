package net.hyren.factions.echo.packet

import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.packets.EchoPacket
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FactionUserUpdateEchoPacket(
    var factionUserId: EntityID<UUID>? = null,
) : EchoPacket() {

    override fun write(
        buffer: EchoBufferOutput
    ) {
        buffer.writeEntityID(factionUserId)
    }

    override fun read(
        buffer: EchoBufferInput
    ) {
        factionUserId = buffer.readEntityID()
    }

}