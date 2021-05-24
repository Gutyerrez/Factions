package net.hyren.factions.user.storage.dao

import net.hyren.core.shared.CoreProvider
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.table.FactionsUsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FactionUserDAO(
    id: EntityID<UUID>
) : UUIDEntity(id) {

    companion object : UUIDEntityClass<FactionUserDAO>(FactionsUsersTable)

    var role by FactionsUsersTable.role
    var factionId by FactionsUsersTable.factionId
    var power by FactionsUsersTable.power
    var maxPower by FactionsUsersTable.maxPower
    var enemyKills by FactionsUsersTable.enemyKills
    var neutralKills by FactionsUsersTable.neutralKills
    var civilianKills by FactionsUsersTable.civilianKills
    var enemyDeaths by FactionsUsersTable.enemyDeaths
    var neutralDeaths by FactionsUsersTable.neutralDeaths
    var civilianDeaths by FactionsUsersTable.civilianDeaths
    var mapAutoUpdating by FactionsUsersTable.mapAutoUpdating
    var seeingChunks by FactionsUsersTable.seeingChunks
    var createdAt by FactionsUsersTable.createdAt
    var updatedAt by FactionsUsersTable.updatedAt

    fun toFactionUser(): FactionUser = FactionUser(
        CoreProvider.Cache.Local.USERS.provide().fetchById(this.id)!!
    ).apply {
        this.role = this@FactionUserDAO.role
        this.factionId = this@FactionUserDAO.factionId
        this.power = this@FactionUserDAO.power
        this.maxPower = this@FactionUserDAO.maxPower
        this.enemyKills = this@FactionUserDAO.enemyKills
        this.neutralKills = this@FactionUserDAO.neutralKills
        this.civilianKills = this@FactionUserDAO.civilianKills
        this.enemyDeaths = this@FactionUserDAO.enemyDeaths
        this.neutralDeaths = this@FactionUserDAO.neutralDeaths
        this.civilianDeaths = this@FactionUserDAO.civilianDeaths
        this.mapAutoUpdating = this@FactionUserDAO.mapAutoUpdating
        this.seeingChunks = this@FactionUserDAO.seeingChunks
        this.createdAt = this@FactionUserDAO.createdAt
        this.updatedAt = this@FactionUserDAO.updatedAt
    }

}