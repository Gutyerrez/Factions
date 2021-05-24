package net.hyren.factions.faction.storage.repositories.implementations

import net.hyren.factions.alpha.FactionsAlphaProvider
import net.hyren.factions.faction.storage.dao.FactionDAO
import net.hyren.factions.faction.storage.dto.*
import net.hyren.factions.faction.storage.repositories.IFactionsRepository
import net.hyren.factions.faction.storage.table.FactionsTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * @author Gutyerrez
 */
class PostgreSQLFactionsRepository : IFactionsRepository {
    override fun fetchById(
        fetchFactionByIdDTO: FetchFactionByIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionDAO.findById(fetchFactionByIdDTO.id)?.toFaction()
    }

    override fun fetchByName(
        fetchFactionByNameDTO: FetchFactionByNameDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionDAO.find {
            FactionsTable.name eq fetchFactionByNameDTO.name
        }.firstOrNull()?.toFaction()
    }

    override fun fetchByTag(
        fetchFactionByTagDTO: FetchFactionByTagDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionDAO.find {
            FactionsTable.tag eq fetchFactionByTagDTO.tag
        }.firstOrNull()?.toFaction()
    }

    override fun create(
        createFactionDTO: CreateFactionDTO
    ) = transaction(
            FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionDAO.new(
            UUID.nameUUIDFromBytes("Faction:${createFactionDTO.name}".toByteArray())
        ) {
            this.name = createFactionDTO.name
            this.tag = createFactionDTO.tag
        }.toFaction()
    }

    override fun update(
        updateFactionDTO: UpdateFactionDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionDAO.findById(updateFactionDTO.id)?.apply {
            updateFactionDTO.update.invoke(this)
        }
    }?.writeValues?.size != 0

    override fun delete(
        deleteFactionDTO: DeleteFactionDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsTable.deleteWhere {
            FactionsTable.id eq deleteFactionDTO.id
        } > 0
    }

}