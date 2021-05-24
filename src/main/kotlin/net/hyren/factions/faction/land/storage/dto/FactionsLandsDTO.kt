package net.hyren.factions.faction.land.storage.dto

import net.hyren.core.shared.CoreConstants
import net.hyren.factions.faction.land.data.LandType
import net.hyren.factions.faction.land.storage.table.FactionsLandsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
open class FetchFactionsLandsByFactionIdDTO(
    val factionId: EntityID<UUID>
)

open class FetchFactionLandByChunkXAndZ(
    val x: Int,
    val z: Int
)

open class CreateFactionLandDTO(
    val factionId: EntityID<UUID>,
    val landType: LandType,
    val worldName: String,
    val x: Int,
    val z: Int
)

open class DeleteFactionLandDTO(
    val factionId: EntityID<UUID>,
    val landType: LandType,
    val worldName: String,
    val x: Int,
    val z: Int
)