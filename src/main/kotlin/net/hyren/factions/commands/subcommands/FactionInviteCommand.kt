package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.commands.Commandable
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.commands.FactionCommand
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionInviteCommand : CustomCommand("convite") {

    override fun getParent() = FactionCommand()

    override fun getSubCommands() = listOf(
        FactionInviteAcceptCommand()
    )

}