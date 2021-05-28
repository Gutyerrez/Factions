package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.commands.argument.Argument
import net.hyren.core.shared.misc.utils.DefaultMessage
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FACTION_NOT_FOUND
import net.hyren.factions.FactionsConstants
import net.hyren.factions.FactionsProvider
import net.hyren.factions.YOU_ALREADY_HAVE_FACTION
import net.hyren.factions.echo.packet.*
import net.hyren.factions.misc.player.list.updatePlayerList
import net.hyren.factions.user.storage.dto.UpdateFactionUserDTO
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FactionInviteAcceptCommand : CustomCommand("aceitar") {

    override fun getParent() = FactionInviteCommand()

    override fun getUsage0(): Array<BaseComponent> = ComponentBuilder(
        "§cUtilize /f convite aceitar <facção>."
    ).create()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
            "faction user is null"
        )
        val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchByName(if (args.size != 1) {
            args.joinToString(" ")
        } else {
            args[0]
        }) ?: FactionsProvider.Cache.Local.FACTION.provide().fetchByTag(args[0])

        if (faction == null) {
            commandSender.sendMessage(DefaultMessage.FACTION_NOT_FOUND)
            return false
        }

        if (factionUser.hasFaction()) {
            commandSender.sendMessage(DefaultMessage.YOU_ALREADY_HAVE_FACTION)
            return false
        }

        println("Convites enviados: " + faction.getSentInvites().size)

        if (!faction.hasInvited(factionUser)) {
            commandSender.sendMessage(
                TextComponent("§cVocê não recebeu um convite para participar dessa facção.")
            )
            return false
        }

        if (faction.getUsersCount() >= faction.maxUsers) {
            commandSender.sendMessage(
                TextComponent("§cEssa facção já atingiu o limite de membros.")
            )
            return false
        }

        CoreProvider.Databases.Redis.ECHO.provide().publishToCurrentServer(
            FactionUserInviteAcceptedEchoPacket(
                factionUser.id,
                faction.id
            )
        )

        factionUser = FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
            UpdateFactionUserDTO(
                factionUser.id
            ) {
                it.factionId = faction.id
                it.role = FactionsConstants.Faction.DEFAULT_ROLE
                it.updatedAt = DateTime.now(
                    CoreConstants.DATE_TIME_ZONE
                )
            }
        )

        CoreProvider.Databases.Redis.ECHO.provide().publishToCurrentServer(
            FactionUserUpdatedEchoPacket(
                factionUser.id
            )
        )

        commandSender.sendMessage(
            TextComponent("§aVocê juntou-se a facção ${faction.fullyQualifiedName}")
        )
        return true
    }

}