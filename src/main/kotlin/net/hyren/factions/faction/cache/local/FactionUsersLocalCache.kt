package net.hyren.factions.faction.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.dto.FetchFactionUsersByFactionIdDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class FactionUsersLocalCache : LocalCache {

    private val CACHE_BY_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, Array<FactionUser>> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().fetchFactionUsersByFactionId(
                FetchFactionUsersByFactionIdDTO(
                    it
                )
            )
        }

    fun fetchByFactionId(factionId: EntityID<UUID>) = CACHE_BY_ID.get(factionId)

    fun fetchByFactionId(factionId: UUID) = CACHE_BY_ID.get(
        EntityID(
            factionId,
            FactionsTable
        )
    )

}