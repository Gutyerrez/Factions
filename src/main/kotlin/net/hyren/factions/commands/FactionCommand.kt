package net.hyren.factions.commands

import net.hyren.core.shared.commands.restriction.CommandRestrictionimport net.hyren.core.shared.users.data.Userimport net.hyren.core.spigot.command.CustomCommandimport net.hyren.factions.FactionsProviderimport net.hyren.factions.commands.subcommands.*import org.bukkit.command.CommandSender

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
            // open inventory
            return false
        }
    }

}