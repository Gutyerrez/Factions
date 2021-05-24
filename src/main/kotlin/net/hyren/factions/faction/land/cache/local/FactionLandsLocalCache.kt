package net.hyren.factions.faction.land.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.factions.FactionsConstants
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.data.Faction
import net.hyren.factions.faction.land.data.FactionLand
import net.hyren.factions.faction.land.data.LandType
import net.hyren.factions.faction.land.storage.dto.FetchFactionLandByChunkXAndZ
import net.hyren.factions.faction.land.storage.dto.FetchFactionsLandsByFactionIdDTO
import net.hyren.factions.faction.storage.table.FactionsTable
import org.bukkit.Bukkit
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class FactionLandsLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<FactionLandLookup, FactionLand?> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_LANDS_REPOSITORY.provide().fetchFactionLandByChunkXAndZ(
                FetchFactionLandByChunkXAndZ(
                    it.x,
                    it.z
                )
            )
        }

    private val CACHE_BY_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, Array<FactionLand>> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_LANDS_REPOSITORY.provide().fetchFactionLandsByFactionId(
                FetchFactionsLandsByFactionIdDTO(
                    it
                )
            )
        }

    private val CACHE_NO_EXPIRE = Caffeine.newBuilder()
        .build<FactionLandLookup, FactionLand>()

    fun fetchByFactionId(factionId: EntityID<UUID>) = CACHE_BY_ID.get(factionId)

    fun fetchByFactionId(factionId: UUID) = CACHE_BY_ID.get(
        EntityID(
            factionId,
            FactionsTable
        )
    )

    fun fetchByXAndZ(x: Int, z: Int) = CACHE.get(
        FactionLandLookup(x, z)
    ) ?: CACHE_NO_EXPIRE.getIfPresent(FactionLandLookup(x, z))

    fun refresh(faction: Faction) = CACHE_BY_ID.refresh(faction.id)

    override fun populate() {
        for (x in FactionsConstants.Faction.WAR_ZONE_CUBOID.minX..FactionsConstants.Faction.WAR_ZONE_CUBOID.maxX) {
            for (z in FactionsConstants.Faction.WAR_ZONE_CUBOID.minZ..FactionsConstants.Faction.WAR_ZONE_CUBOID.maxZ) {
                CACHE_NO_EXPIRE.put(
                    FactionLandLookup(x, z),
                    FactionLand(
                        FactionsConstants.Faction.WAR_ZONE_ID,
                        LandType.WAR,
                        Bukkit.getWorlds()[0].name,
                        x,
                        z,
                        DateTime.now(
                            CoreConstants.DATE_TIME_ZONE
                        )
                    )
                )
            }
        }
    }

}

internal data class FactionLandLookup(
    val x: Int,
    val z: Int
) {

    override fun hashCode(): Int {
        return x + z
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as FactionLandLookup

        if (x != other.x) return false

        if (z != other.z) return false

        return true
    }

}