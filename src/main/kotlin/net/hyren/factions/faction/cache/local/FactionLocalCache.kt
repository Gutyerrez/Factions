package net.hyren.factions.faction.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.data.Faction
import net.hyren.factions.faction.storage.dto.FetchFactionByIdDTO
import net.hyren.factions.faction.storage.dto.FetchFactionByNameDTO
import net.hyren.factions.faction.storage.dto.FetchFactionByTagDTO
import net.hyren.factions.faction.storage.table.FactionsTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class FactionLocalCache : LocalCache {

    private val CACHE_BY_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, Faction> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().fetchById(
                FetchFactionByIdDTO(
                    it
                )
            )
        }

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<String, Faction> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().fetchByName(
                FetchFactionByNameDTO(
                    it
                )
            )
        }

    private val CACHE_BY_TAG = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<String, Faction> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().fetchByTag(
                FetchFactionByTagDTO(
                    it
                )
            )
        }

    fun fetchById(factionId: EntityID<UUID>?) = factionId.run {
        if (this == null) {
            println("Id nullo $this")

            null
        } else {
            println("DALE $this")

            CACHE_BY_ID.get(this)
        }
    }

    fun fetchById(factionId: UUID) = CACHE_BY_ID.get(
        EntityID(
            factionId,
            FactionsTable
        )
    )

    fun fetchByName(name: String) = CACHE_BY_NAME.get(name)

    fun fetchByTag(tag: String) = CACHE_BY_TAG.get(tag)

}