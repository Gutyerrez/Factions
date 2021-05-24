package net.hyren.factions.faction.storage.dao

import net.hyren.factions.faction.data.Faction
import net.hyren.factions.faction.storage.table.FactionsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
class FactionDAO(
    id: EntityID<UUID>
) : UUIDEntity(id) {

    companion object : UUIDEntityClass<FactionDAO>(FactionsTable)

    var name by FactionsTable.name
    var tag by FactionsTable.tag
    var warWins by FactionsTable.warWins
    var base by FactionsTable.base
    var outPosting by FactionsTable.outPosting
    var createdAt by FactionsTable.createdAt
    var updatedAt by FactionsTable.updatedAt

    fun toFaction() = Faction(
        this.id,
        this.name,
        this.tag,
        this.warWins,
        this.base,
        this.outPosting,
        this.createdAt,
        this.updatedAt
    )

}