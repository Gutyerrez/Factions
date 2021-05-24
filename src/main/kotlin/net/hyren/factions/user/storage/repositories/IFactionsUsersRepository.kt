package net.hyren.factions.user.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.dto.*

/**
 * @author Gutyerrez
 */
interface IFactionsUsersRepository : IRepository {

    fun fetchByUserId(
        fetchFactionUserByUserId: FetchFactionUserByUserIdDTO
    ): FactionUser?

    fun fetchByUserName(
        fetchFactionUserByUserName: FetchFactionUserByUserNameDTO
    ): FactionUser?

    fun fetchFactionUsersByFactionId(
        fetchFactionUsersByFactionIdDTO: FetchFactionUsersByFactionIdDTO
    ): Array<FactionUser>

    fun create(
        createFactionUserDTO: CreateFactionUserDTO
    ): FactionUser

    fun update(
        updateFactionUserDTO: UpdateFactionUserDTO
    ): FactionUser

}