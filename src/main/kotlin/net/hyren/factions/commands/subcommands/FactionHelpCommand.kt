package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.commands.Commandable
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.commands.FactionCommand
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionHelpCommand : CustomCommand("ajuda") {

    override fun getParent() = FactionCommand()

    override fun getAliases0() = arrayOf(
        "?"
    )

    override fun getDescription() = "Mostrar essa página."

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        sendAvailableCommands(commandSender)
        return true
    }

}