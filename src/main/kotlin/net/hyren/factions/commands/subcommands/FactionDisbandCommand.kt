package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.commands.Commandable
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.commands.restriction.FactionNeededCommandRestrictable
import net.hyren.factions.faction.storage.dto.DeleteFactionDTO
import net.hyren.factions.user.data.FactionUser
import org.bukkit.command.CommandSender

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
            commandSender.sendMessage("Facção desfeita!")
            true
        } else {
            commandSender.sendMessage("Não foi possível desfazer a facção, será que ela já não está desfeita?")
            false
        }
    }

}