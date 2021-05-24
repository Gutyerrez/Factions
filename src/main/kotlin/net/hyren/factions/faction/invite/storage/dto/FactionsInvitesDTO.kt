package net.hyren.factions.faction.invite.storage.dto

import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
open class FetchFactionsInvitesByFactionIdDTO(
    val factionId: EntityID<UUID>
)

open class FetchFactionsInvitesByFactionUserIdDTO(
    val factionUserId: EntityID<UUID>
)

open class FetchFactionsInviteByFactionIdAndFactionUserIdDTO(
    val factionId: EntityID<UUID>,
    val factionUserId: EntityID<UUID>
)

open class CreateFactionInviteDTO(
    val factionId: EntityID<UUID>,
    val factionUserId: EntityID<UUID>
)

open class DeleteFactionInviteDTO(
    val factionId: EntityID<UUID>,
    val factionUserId: EntityID<UUID>
)