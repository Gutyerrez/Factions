package net.hyren.factions.faction.invite.storage.repositories.implementations

import net.hyren.factions.alpha.FactionsAlphaProvider
import net.hyren.factions.faction.invite.storage.dto.*
import net.hyren.factions.faction.invite.storage.repositories.IFactionsInvitesRepository
import net.hyren.factions.faction.invite.storage.table.FactionsInvitesTable
import net.hyren.factions.faction.invite.storage.table.FactionsInvitesTable.toFactionInvite
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgreSQLFactionsInvitesRepository : IFactionsInvitesRepository {

    override fun fetchFactionsInvitesByFactionId(
        fetchFactionsInvitesByFactionIdDTO: FetchFactionsInvitesByFactionIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsInvitesTable.select {
            FactionsInvitesTable.factionId eq fetchFactionsInvitesByFactionIdDTO.factionId
        }.map { it.toFactionInvite() }.toTypedArray()
    }

    override fun fetchFactionsInvitesByFactionUserId(
        fetchFactionsInvitesByFactionUserIdDTO: FetchFactionsInvitesByFactionUserIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsInvitesTable.select {
            FactionsInvitesTable.factionUserId eq fetchFactionsInvitesByFactionUserIdDTO.factionUserId
        }.map { it.toFactionInvite() }.toTypedArray()
    }

    override fun create(
        createFactionInviteDTO: CreateFactionInviteDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsInvitesTable.insert {
            it[factionId] = createFactionInviteDTO.factionId
            it[factionUserId] = createFactionInviteDTO.factionUserId
        }.resultedValues!!.first().toFactionInvite()
    }

    override fun delete(
        deleteFactionInviteDTO: DeleteFactionInviteDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionsInvitesTable.deleteWhere {
            FactionsInvitesTable.factionUserId eq deleteFactionInviteDTO.factionId and (
                FactionsInvitesTable.factionUserId eq deleteFactionInviteDTO.factionUserId
            )
        } > 0
    }

}