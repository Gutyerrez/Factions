package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.*
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.commands.restriction.FactionNeededCommandRestrictable
import net.hyren.factions.echo.packet.FactionUserUpdatedEchoPacket
import net.hyren.factions.faction.storage.dto.DeleteFactionDTO
import net.hyren.factions.user.storage.dto.UpdateFactionUserDTO
import org.bukkit.command.CommandSender
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FactionDisbandCommand : CustomCommand("desfazer"), FactionNeededCommandRestrictable {

    override fun getParent() = FactionCommand()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        // create inventory to disband faction

        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
            "faction user is null"
        )

        return if (FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().delete(
                DeleteFactionDTO(
                    factionUser.factionId!!
                )
            )
        ) {
            factionUser.getFaction()?.getUsers()?.forEach { factionUser ->
                FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
                    UpdateFactionUserDTO(
                        factionUser.id
                    ) {
                        it.role = null
                        it.factionId = null
                        it.updatedAt = DateTime.now(
                            CoreConstants.DATE_TIME_ZONE
                        )
                    }
                )
            }

            CoreProvider.Databases.Redis.ECHO.provide().publishToCurrentServer(
                FactionUserUpdatedEchoPacket(
                    factionUser.id
                )
            )

            commandSender.sendMessage("Facção desfeita!")
            true
        } else {
            commandSender.sendMessage("Não foi possível desfazer a facção, será que ela já não está desfeita?")
            false
        }
    }

}