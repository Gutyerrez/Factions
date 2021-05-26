package net.hyren.factions.user.storage.dto

import net.hyren.factions.FactionsConstants
import net.hyren.factions.user.storage.dao.FactionUserDAO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
open class FetchFactionUserByUserIdDTO(
    val userId: EntityID<UUID>
)

open class FetchFactionUserByUserNameDTO(
    val name: String
)

open class FetchFactionUsersByFactionIdDTO(
    val factionId: EntityID<UUID>
)

open class CreateFactionUserDTO(
    val userId: EntityID<UUID>,
    val power: Double = FactionsConstants.FactionUser.DEFAULT_FACTION_USER_POWER,
    val maxPower: Double = FactionsConstants.FactionUser.DEFAULT_FACTION_USER_MAX_POWER,
    val execute: (FactionUserDAO) -> Unit = { /* nothing */ }
)

open class UpdateFactionUserDTO(
    val userId: EntityID<UUID>,
    val update: (FactionUserDAO) -> Unit
)
