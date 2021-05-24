package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.misc.utils.drawMap
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionMapCommand : CustomCommand("mapa") {

    override fun getParent() = FactionCommand()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
            "faction user is null"
        )

        commandSender.sendMessage(
            factionUser.drawMap()
        )
        return true
    }

}