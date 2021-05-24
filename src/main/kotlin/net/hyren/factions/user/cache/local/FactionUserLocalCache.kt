package net.hyren.factions.user.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.users.storage.table.UsersTable
import net.hyren.factions.FactionsProvider
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.dto.FetchFactionUserByUserIdDTO
import net.hyren.factions.user.storage.dto.FetchFactionUserByUserNameDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class FactionUserLocalCache : LocalCache {

    private val CACHE_BY_ID = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<EntityID<UUID>, FactionUser> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().fetchByUserId(
                FetchFactionUserByUserIdDTO(
                    it
                )
            )
        }

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .build<String, FactionUser> {
            FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().fetchByUserName(
                FetchFactionUserByUserNameDTO(
                    it
                )
            )
        }

    fun fetchByUserId(userId: EntityID<UUID>) = CACHE_BY_ID.get(userId)?.let {
        return@let CoreProvider.Cache.Local.USERS.provide().fetchById(userId)?.let factionUser@ {
            if (it.getConnectedBukkitApplication()?.server == CoreProvider.application.server) {
                return@factionUser FactionUser(it)
            } else return@factionUser null
        }
    }

    fun fetchByUserId(userId: UUID) = CACHE_BY_ID.get(
        EntityID(
            userId,
            UsersTable
        )
    )?.let {
        return@let CoreProvider.Cache.Local.USERS.provide().fetchById(EntityID(userId, UsersTable))?.let factionUser@ {
            if (it.getConnectedBukkitApplication()?.server == CoreProvider.application.server) {
                return@factionUser FactionUser(it)
            } else return@factionUser null
        }
    }

    fun fetchByUserName(userName: String) = CACHE_BY_NAME.get(userName)?.let {
        return@let CoreProvider.Cache.Local.USERS.provide().fetchByName(userName)?.let factionUser@ {
            if (it.getConnectedBukkitApplication()?.server == CoreProvider.application.server) {
                return@factionUser FactionUser(it)
            } else return@factionUser null
        }
    }

    fun refresh(factionUser: FactionUser) {
        CACHE_BY_ID.refresh(factionUser.id)
        CACHE_BY_NAME.refresh(factionUser.name)
    }

}