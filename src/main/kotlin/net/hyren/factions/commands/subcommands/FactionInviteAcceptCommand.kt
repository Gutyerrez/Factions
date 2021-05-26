package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreConstantsimport net.hyren.core.shared.CoreProviderimport net.hyren.core.shared.commands.argument.Argumentimport net.hyren.core.shared.misc.utils.DefaultMessageimport net.hyren.core.shared.users.data.Userimport net.hyren.core.spigot.command.CustomCommandimport net.hyren.factions.FACTION_NOT_FOUNDimport net.hyren.factions.FactionsConstantsimport net.hyren.factions.FactionsProviderimport net.hyren.factions.YOU_ALREADY_HAVE_FACTIONimport net.hyren.factions.echo.packet.FactionUserInviteAcceptedEchoPacketimport net.hyren.factions.user.storage.dto.UpdateFactionUserDTOimport net.md_5.bungee.api.chat.TextComponentimport org.bukkit.command.CommandSenderimport org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FactionInviteAcceptCommand : CustomCommand("aceitar") {

    override fun getParent() = FactionInviteCommand()

    override fun getArguments() = listOf(
        Argument("facção")
    )

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
            "faction user is null"
        )
        val faction = FactionsProvider.Cache.Local.FACTION.provide().fetchByName(args[0])

        if (faction == null) {
            commandSender.sendMessage(
                DefaultMessage.FACTION_NOT_FOUND
            )
            return false
        }

        if (factionUser.hasFaction()) {
            commandSender.sendMessage(
                DefaultMessage.YOU_ALREADY_HAVE_FACTION
            )
            return false
        }

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

        // Refresh cache
        FactionsProvider.Cache.Local.FACTION_USER.provide().refresh(factionUser)
        FactionsProvider.Cache.Local.FACTION_INVITES.provide().refresh(factionUser)

        commandSender.sendMessage(
            TextComponent("§aVocê juntou-se a facção ${faction.fullyQualifiedName}")
        )
        return true
    }

}