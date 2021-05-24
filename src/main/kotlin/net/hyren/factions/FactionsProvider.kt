package net.hyren.factions

import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.databases.postgresql.providers.PostgreSQLRepositoryProvider
import net.hyren.factions.faction.cache.local.FactionLocalCache
import net.hyren.factions.faction.cache.local.FactionUsersLocalCache
import net.hyren.factions.faction.invite.cache.local.FactionInvitesLocalCache
import net.hyren.factions.faction.invite.storage.repositories.IFactionsInvitesRepository
import net.hyren.factions.faction.invite.storage.repositories.implementations.PostgreSQLFactionsInvitesRepository
import net.hyren.factions.faction.land.cache.local.FactionLandsLocalCache
import net.hyren.factions.faction.land.storage.repositories.IFactionsLandsRepository
import net.hyren.factions.faction.land.storage.repositories.implementations.PostgreSQLFactionsLandsRepository
import net.hyren.factions.faction.storage.repositories.IFactionsRepository
import net.hyren.factions.faction.storage.repositories.implementations.PostgreSQLFactionsRepository
import net.hyren.factions.user.cache.local.FactionUserLocalCache
import net.hyren.factions.user.storage.repositories.IFactionsUsersRepository
import net.hyren.factions.user.storage.repositories.implementations.PostgreSQLFactionsUsersRepository

/**
 * @author Gutyerrez
 */
object FactionsProvider {

    fun prepare() {
        // Factions repository
        Repositories.PostgreSQL.FACTIONS_REPOSITORY.prepare()
        Repositories.PostgreSQL.FACTIONS_LANDS_REPOSITORY.prepare()
        Repositories.PostgreSQL.FACTIONS_INVITES_REPOSITORY.prepare()

        // Users repository
        Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.prepare()

        // Factions cache
        Cache.Local.FACTION.prepare()
        Cache.Local.FACTION_USERS.prepare()
        Cache.Local.FACTION_LANDS.prepare()
        Cache.Local.FACTION_INVITES.prepare()

        // Users cache
        Cache.Local.FACTION_USER.prepare()
    }

    object Repositories {

        object PostgreSQL {

            val FACTIONS_REPOSITORY = PostgreSQLRepositoryProvider<IFactionsRepository>(
                PostgreSQLFactionsRepository::class
            )

            val FACTIONS_LANDS_REPOSITORY = PostgreSQLRepositoryProvider<IFactionsLandsRepository>(
                PostgreSQLFactionsLandsRepository::class
            )

            val FACTIONS_INVITES_REPOSITORY = PostgreSQLRepositoryProvider<IFactionsInvitesRepository>(
                PostgreSQLFactionsInvitesRepository::class
            )

            val FACTIONS_USERS_REPOSITORY = PostgreSQLRepositoryProvider<IFactionsUsersRepository>(
                PostgreSQLFactionsUsersRepository::class
            )

        }

    }

    object Cache {

        object Local {

            val FACTION = LocalCacheProvider(
                FactionLocalCache()
            )

            val FACTION_USERS = LocalCacheProvider(
                FactionUsersLocalCache()
            )

            val FACTION_LANDS = LocalCacheProvider(
                FactionLandsLocalCache()
            )

            val FACTION_INVITES = LocalCacheProvider(
                FactionInvitesLocalCache()
            )

            val FACTION_USER = LocalCacheProvider(
                FactionUserLocalCache()
            )

        }

    }

    object Controllers {

        object FactionController {

            fun isFactionTagTaken(tag: String) = Cache.Local.FACTION.provide().fetchByTag(tag) != null

            fun isFactionNameTaken(name: String) = Cache.Local.FACTION.provide().fetchByName(name) != null

        }

    }

}