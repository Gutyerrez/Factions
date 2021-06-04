package net.hyren.factions.user.storage.table

import net.hyren.core.shared.CoreConstants
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.role.Role
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
object FactionsUsersTable : UUIDTable("factions_users", "user_id") {

    val role = enumerationByName("role", 255, Role::class).nullable()
    val factionId = reference("faction_id", FactionsTable).nullable()
    val power = double("power")
    val maxPower = double("max_power")
    val enemyKills = integer("enemy_kills").default(0)
    val neutralKills = integer("neutral_kills").default(0)
    val civilianKills = integer("civilian_kills").default(0)
    val enemyDeaths = integer("enemy_deaths").default(0)
    val neutralDeaths = integer("neutral_deaths").default(0)
    val civilianDeaths = integer("civilian_deaths").default(0)
    val mapAutoUpdating = bool("is_map_auto_updating").default(false)
    val seeingChunks = bool("is_seeing_chunks").default(false)
    val createdAt = datetime("created_at").default(
        DateTime.now(
            CoreConstants.DATE_TIME_ZONE
        )
    )
    val updatedAt = datetime("updated_at").nullable()

}