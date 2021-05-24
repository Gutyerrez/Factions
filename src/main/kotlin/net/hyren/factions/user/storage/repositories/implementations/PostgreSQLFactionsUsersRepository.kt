package net.hyren.factions.user.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.factions.alpha.FactionsAlphaProvider
import net.hyren.factions.user.storage.dao.FactionUserDAO
import net.hyren.factions.user.storage.dto.*
import net.hyren.factions.user.storage.repositories.IFactionsUsersRepository
import net.hyren.factions.user.storage.table.FactionsUsersTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgreSQLFactionsUsersRepository : IFactionsUsersRepository {

    override fun fetchByUserId(
        fetchFactionUserByUserId: FetchFactionUserByUserIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionUserDAO.findById(fetchFactionUserByUserId.userId)?.toFactionUser()
    }

    override fun fetchByUserName(
        fetchFactionUserByUserName: FetchFactionUserByUserNameDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        CoreProvider.Cache.Local.USERS.provide().fetchByName(fetchFactionUserByUserName.name)?.let {
            FactionUserDAO.findById(it.id)?.toFactionUser()
        }
    }

    override fun fetchFactionUsersByFactionId(
        fetchFactionUsersByFactionIdDTO: FetchFactionUsersByFactionIdDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionUserDAO.find {
            FactionsUsersTable.factionId eq fetchFactionUsersByFactionIdDTO.factionId
        }.map { it.toFactionUser() }.toTypedArray()
    }

    override fun create(
        createFactionUserDTO: CreateFactionUserDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionUserDAO.new(createFactionUserDTO.userId.value) {
            power = createFactionUserDTO.power
            maxPower = createFactionUserDTO.maxPower
        }.toFactionUser()
    }

    override fun update(
        updateFactionUserDTO: UpdateFactionUserDTO
    ) = transaction(
        FactionsAlphaProvider.Databases.PostgreSQL.POSTGRESQL_FACTIONS_ALPHA.provide()
    ) {
        FactionUserDAO.findById(updateFactionUserDTO.userId)?.apply {
            updateFactionUserDTO.update.invoke(this)
        }?.toFactionUser() ?: create(
            CreateFactionUserDTO(
                updateFactionUserDTO.userId
            )
        )
    }

}