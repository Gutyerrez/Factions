package net.hyren.factions.echo.packet

import net.hyren.core.shared.echo.api.buffer.*
import net.hyren.core.shared.echo.api.packets.EchoPacket
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FactionUserUpdatedEchoPacket(
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