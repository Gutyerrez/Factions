package net.hyren.factions.user.storage.table

import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.role.Role
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author Gutyerrez
 */
object FactionsUsersTable : UUIDTable("factions_users", "user_id") {

    val role = enumerationByName("role", 255, Role::class).nullable()
    val factionId = reference("faction_id", FactionsTable).nullable()
    val power = double("power")
    val maxPower = double("max_power")
    val enemyKills = integer("enemy_kills")
    val neutralKills = integer("neutral_kills")
    val civilianKills = integer("civilian_kills")
    val enemyDeaths = integer("enemy_deaths")
    val neutralDeaths = integer("neutral_deaths")
    val civilianDeaths = integer("civilian_deaths")
    val mapAutoUpdating = bool("map_auto_updating")
    val seeingChunks = bool("seeing_chunks")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()

}