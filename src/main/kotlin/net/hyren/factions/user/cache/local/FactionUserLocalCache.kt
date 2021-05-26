package net.hyren.factions.user.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.users.storage.table.UsersTable
import net.hyren.factions.FactionsProvider
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.dto.*
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

    fun fetchByUserId(userId: EntityID<UUID>) = if (CACHE_BY_ID.get(userId) == null) {
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        if (user == null) {
            null
        } else FactionUser(user)
    } else {
        CACHE_BY_ID.get(userId)
    }

    fun fetchByUserId(userId: UUID) = if (CACHE_BY_ID.get(
        EntityID(
            userId,
            UsersTable
        )
    ) == null) {
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        if (user == null) {
            null
        } else FactionUser(user)
    } else {
        CACHE_BY_ID.get(
            EntityID(
                userId,
                UsersTable
            )
        )
    }

    fun fetchByUserName(userName: String) = if (CACHE_BY_NAME.get(userName) == null) {
        val user = CoreProvider.Cache.Local.USERS.provide().fetchByName(userName)

        if (user == null) {
            null
        } else FactionUser(user)
    } else {
        CACHE_BY_NAME.get(userName)
    }

    fun refresh(factionUser: FactionUser) {
        CACHE_BY_ID.refresh(factionUser.id)
        CACHE_BY_NAME.refresh(factionUser.name)
    }

}