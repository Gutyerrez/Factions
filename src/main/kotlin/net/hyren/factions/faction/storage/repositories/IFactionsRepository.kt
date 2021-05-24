package net.hyren.factions.faction.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.factions.faction.data.Faction
import net.hyren.factions.faction.storage.dto.*

/**
 * @author Gutyerrez
 */
interface IFactionsRepository : IRepository {

    fun fetchById(
        fetchFactionByIdDTO: FetchFactionByIdDTO
    ): Faction?

    fun fetchByName(
        fetchFactionByNameDTO: FetchFactionByNameDTO
    ): Faction?

    fun fetchByTag(
        fetchFactionByTagDTO: FetchFactionByTagDTO
    ): Faction?

    fun create(
        createFactionDTO: CreateFactionDTO
    ): Faction?

    fun delete(
        deleteFactionDTO: DeleteFactionDTO
    ): Boolean

    fun update(
        updateFactionDTO: UpdateFactionDTO
    ): Boolean

}