package net.hyren.factions.commands

import net.hyren.core.shared.commands.restriction.CommandRestriction
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.subcommands.*
import net.hyren.factions.inventories.FactionInventory
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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
        FactionInfoCommand(),
        FactionProfileCommand(),
        FactionInviteAddCommand()
    )

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        commandSender as Player

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
                    val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserName(args[0])

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
            val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user.id!!)

            commandSender.openInventory(FactionInventory(factionUser))
            return false
        }
    }

}
