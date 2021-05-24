package net.hyren.factions.faction.invite.data

import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
data class FactionInvite(
    val factionId: EntityID<UUID>,
    val factionUserId: EntityID<UUID>,
    val createdAt: DateTime
)
