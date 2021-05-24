package net.hyren.factions.faction.land.storage.table

import net.hyren.core.shared.CoreConstants
import net.hyren.factions.faction.land.data.FactionLand
import net.hyren.factions.faction.land.data.LandType
import net.hyren.factions.faction.storage.table.FactionsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
object FactionsLandsTable : Table("factions_lands") {

    val factionId = reference("faction_id", FactionsTable)
    val landType = enumerationByName("land_type", 255, LandType::class)
    val worldName = varchar("world_name", 255)
    val x = integer("x")
    val z = integer("z")
    val createdAt = datetime("created_at").default(
        DateTime.now(
            CoreConstants.DATE_TIME_ZONE
        )
    )
    var updatedAt = datetime("updated_at").nullable()

    fun ResultRow.toFactionLand() = FactionLand(
        this[factionId],
        this[landType],
        this[worldName],
        this[x],
        this[z],
        this[createdAt],
        this[updatedAt]
    )

}
