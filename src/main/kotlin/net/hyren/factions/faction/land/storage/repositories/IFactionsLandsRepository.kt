package net.hyren.factions.faction.land.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.factions.faction.land.data.FactionLand
import net.hyren.factions.faction.land.storage.dto.CreateFactionLandDTO
import net.hyren.factions.faction.land.storage.dto.DeleteFactionLandDTO
import net.hyren.factions.faction.land.storage.dto.FetchFactionLandByChunkXAndZ
import net.hyren.factions.faction.land.storage.dto.FetchFactionsLandsByFactionIdDTO

/**
 * @author Gutyerrez
 */
interface IFactionsLandsRepository : IRepository {

    fun fetchFactionLandsByFactionId(
        fetchFactionsLandsByFactionIdDTO: FetchFactionsLandsByFactionIdDTO
    ): Array<FactionLand>

    fun fetchFactionLandByChunkXAndZ(
        fetchFactionLandByChunkXAndZ: FetchFactionLandByChunkXAndZ
    ): FactionLand?

    fun create(
        createFactionLandDTO: CreateFactionLandDTO
    ): FactionLand

    fun delete(
        deleteFactionLandDTO: DeleteFactionLandDTO
    ): Boolean

}