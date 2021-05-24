package net.hyren.factions.faction.invite.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.invite.data.FactionInvite
import net.hyren.factions.faction.invite.storage.dto.FetchFactionsInvitesByFactionIdDTO
import net.hyren.factions.faction.invite.storage.dto.FetchFactionsInvitesByFactionUserIdDTO
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.table.FactionsUsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class FactionInvitesLocalCache : LocalCache {

    private val CACHE_BY_FACTION_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, Array<FactionInvite>> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_INVITES_REPOSITORY.provide().fetchFactionsInvitesByFactionId(
                FetchFactionsInvitesByFactionIdDTO(
                    it
                )
            )
        }

    private val CACHE_BY_FACTION_USER_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, Array<FactionInvite>> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_INVITES_REPOSITORY.provide().fetchFactionsInvitesByFactionUserId(
                FetchFactionsInvitesByFactionUserIdDTO(
                    it
                )
            )
        }

    fun fetchByFactionId(factionId: EntityID<UUID>) = CACHE_BY_FACTION_ID.get(factionId)

    fun fetchByFactionId(factionId: UUID) = CACHE_BY_FACTION_ID.get(
        EntityID(
            factionId,
            FactionsTable
        )
    )

    fun fetchByFactionUserId(factionUserId: EntityID<UUID>) = CACHE_BY_FACTION_USER_ID.get(factionUserId)

    fun fetchByFactionUserId(factionUserId: UUID) = CACHE_BY_FACTION_USER_ID.get(
        EntityID(
            factionUserId,
            FactionsUsersTable
        )
    )

    fun refresh(factionUser: FactionUser) {
        CACHE_BY_FACTION_USER_ID.refresh(factionUser.id)
        factionUser.factionId?.let {
            CACHE_BY_FACTION_ID.refresh(it)
        }
    }

}