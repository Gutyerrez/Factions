package net.hyren.factions.faction.storage.table

import net.hyren.core.shared.misc.exposed.json
import net.hyren.core.shared.world.location.SerializedLocation
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author Gutyerrez
 */
object FactionsTable : UUIDTable("factions") {

    val name = varchar("name", 20)
    val tag = varchar("tag", 3)
    val warWins = integer("war_wins").default(0)
    val base = json<SerializedLocation>("base", SerializedLocation::class).nullable()
    val outPosting = bool("out_posting").default(false)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()

}