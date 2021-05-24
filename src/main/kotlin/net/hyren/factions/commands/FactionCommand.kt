package net.hyren.factions.commands

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.commands.restriction.CommandRestriction
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.subcommands.*
import net.hyren.factions.user.data.FactionUser
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionCommand : CustomCommand("f") {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getSubCommands() = listOf(
        FactionCreateCommand(),
        FactionDisbandCommand(),
        FactionMapCommand(),
        FactionInviteCommand(),
        FactionInviteAddCommand(),
        FactionInfoCommand(),
        FactionProfileCommand()
    )

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        if (args.size == 1) {
            return when (args[0].length) {
                3 -> {
                    val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchByTag(args[0])

                    if (faction == null) {
                        commandSender.sendMessage("Facção não existe.")
                        false
                    } else {
                        commandSender.sendMessage("Facção existe.")
                        true
                    }
                }
                else -> {
                    val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserName(args[0])?.let {
                        return@let CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])?.let factionUser@{
                            if (it.getConnectedBukkitApplication()?.server == CoreProvider.application.server) {
                                return@factionUser FactionUser(it)
                            } else return@factionUser null
                        }
                    }

                    if (factionUser == null) {
                        commandSender.sendMessage("Usuário existe.")
                        false
                    } else {
                        commandSender.sendMessage("Usuário não existe.")
                        true
                    }
                }
            }
        } else {
            // open inventory
            return false
        }
    }

}