package net.hyren.factions.faction.land.storage.repositories.implementations

import net.hyren.factions.alpha.FactionsAlphaProvider
import net.hyren.factions.faction.land.data.FactionLand
import net.hyren.factions.faction.land.storage.dto.CreateFactionLandDTO
import net.hyren.factions.faction.land.storage.dto.DeleteFactionLandDTO
import net.hyren.factions.faction.land.storage.dto.FetchFactionLandByChunkXAndZ
import net.hyren.factions.faction.land.storage.dto.FetchFactionsLandsByFactionIdDTO
import net.hyren.factions.faction.land.storage.repositories.IFactionsLandsRepository
import net.hyren.factions.faction.land.storage.table.FactionsLandsTable
import net.hyren.factions.faction.land.storage.table.FactionsLandsTable.toFactionLand
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

/**
 * @author Gutyerrez
 */
class PostgreSQLFactionsLandsRepository : IFactionsLandsRepository {

    override fun fetchFactionLandsByFactionId(
        fetchFactionsLandsByFactionIdDTO: FetchFactionsLandsByFactionIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsLandsTable.select {
            FactionsLandsTable.factionId eq fetchFactionsLandsByFactionIdDTO.factionId
        }.map { it.toFactionLand() }.toTypedArray()
    }

    override fun fetchFactionLandByChunkXAndZ(
        fetchFactionLandByChunkXAndZ: FetchFactionLandByChunkXAndZ
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsLandsTable.select {
            FactionsLandsTable.x eq fetchFactionLandByChunkXAndZ.x and (
                FactionsLandsTable.z eq fetchFactionLandByChunkXAndZ.z
            )
        }.firstOrNull()?.toFactionLand()
    }

    override fun create(
        createFactionLandDTO: CreateFactionLandDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsLandsTable.insert {
            it[factionId] = createFactionLandDTO.factionId
            it[landType] = createFactionLandDTO.landType
            it[worldName] = createFactionLandDTO.worldName
            it[x] = createFactionLandDTO.x
            it[z] = createFactionLandDTO.z
        }.resultedValues?.firstOrNull()?.toFactionLand() ?: throw SQLException(
            "cannot insert faction land to faction ${
                createFactionLandDTO.factionId.value
            } at { X: ${
                createFactionLandDTO.x
            }, Z: ${
                createFactionLandDTO.z
            } }"
        )
    }

    override fun delete(
        deleteFactionLandDTO: DeleteFactionLandDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsLandsTable.deleteWhere {
            FactionsLandsTable.factionId eq deleteFactionLandDTO.factionId and (
                FactionsLandsTable.landType eq deleteFactionLandDTO.landType
            ) and (
                FactionsLandsTable.worldName eq deleteFactionLandDTO.worldName
            ) and (
                FactionsLandsTable.x eq deleteFactionLandDTO.x
            ) and (
                FactionsLandsTable.z eq deleteFactionLandDTO.z
            )
        } > 0
    }

}