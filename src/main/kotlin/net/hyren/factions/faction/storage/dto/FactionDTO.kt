package net.hyren.factions.faction.storage.dto

import net.hyren.factions.faction.storage.dao.FactionDAO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
open class FetchFactionByIdDTO(
    val id: EntityID<UUID>
)

open class FetchFactionByNameDTO(
    val name: String
)

open class FetchFactionByTagDTO(
    val tag: String
)

open class CreateFactionDTO(
    val name: String,
    val tag: String
)

open class UpdateFactionDTO(
    val id: EntityID<UUID>,
    val update: FactionDAO.() -> Int
)

open class DeleteFactionDTO(
    val id: EntityID<UUID>
)