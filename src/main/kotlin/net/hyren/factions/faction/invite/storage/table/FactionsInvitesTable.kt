package net.hyren.factions.faction.invite.storage.table

import net.hyren.factions.faction.invite.data.FactionInvite
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.storage.table.FactionsUsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author Gutyerrez
 */
object FactionsInvitesTable : Table("factions_invites") {

    val factionId = reference("faction_id", FactionsTable)
    val factionUserId = reference("faction_user_id", FactionsUsersTable)
    val createdAt = datetime("created_at")

    fun ResultRow.toFactionInvite() = FactionInvite(
        this[factionId],
        this[factionUserId],
        this[createdAt]
    )

}