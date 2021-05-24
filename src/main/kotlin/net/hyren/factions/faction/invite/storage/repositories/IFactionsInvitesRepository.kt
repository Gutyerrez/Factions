package net.hyren.factions.faction.invite.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.factions.faction.invite.data.FactionInvite
import net.hyren.factions.faction.invite.storage.dto.*

/**
 * @author Gutyerrez
 */
interface IFactionsInvitesRepository : IRepository {

    fun fetchFactionsInvitesByFactionId(
        fetchFactionsInvitesByFactionIdDTO: FetchFactionsInvitesByFactionIdDTO
    ): Array<FactionInvite>

    fun fetchFactionsInvitesByFactionUserId(
        fetchFactionsInvitesByFactionUserIdDTO: FetchFactionsInvitesByFactionUserIdDTO
    ): Array<FactionInvite>

    fun create(
        createFactionInviteDTO: CreateFactionInviteDTO
    ): FactionInvite

    fun delete(
        deleteFactionInviteDTO: DeleteFactionInviteDTO
    ): Boolean

}