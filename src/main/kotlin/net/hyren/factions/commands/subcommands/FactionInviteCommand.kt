package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.commands.Commandable
import net.hyren.core.shared.commands.argument.Argument
import net.hyren.core.shared.misc.utils.DefaultMessage
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.*
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.echo.packet.FactionUserInvitedEchoPacket
import net.hyren.factions.faction.invite.storage.dto.CreateFactionInviteDTO
import net.hyren.factions.user.storage.dto.*
import net.md_5.bungee.api.chat.*
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionInviteCommand : CustomCommand("convidar") {

    override fun getParent() = FactionCommand()

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getDescription0() = "Convidar um jogador para sua facção."

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id)!!
        var targetFactionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserName(args[0])

        if (targetFactionUser == null) {
            commandSender.sendMessage(
                DefaultMessage.USER_NOT_FOUND
            )
            return false
        }

        return when (targetFactionUser.hasFaction()) {
            true -> {
                if (targetFactionUser.factionId == factionUser.factionId) {
                    commandSender.sendMessage(
                        TextComponent("§cO usuário já pertence a sua facção.")
                    )
                    return false
                }

                commandSender.sendMessage(DefaultMessage.ALREADY_HAVE_FACTION)
                return false
            }
            false -> {
                val factionUserFromDatabase = FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().fetchByUserId(
                    FetchFactionUserByUserIdDTO(
                        targetFactionUser.id
                    )
                )

                if (factionUserFromDatabase == null) {
                    targetFactionUser = FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
                        UpdateFactionUserDTO(
                            targetFactionUser.id
                        ) { /* nothing */ }
                    )
                }

                val factionInvite = FactionsProvider.Repositories.PostgreSQL.FACTIONS_INVITES_REPOSITORY.provide().create(
                    CreateFactionInviteDTO(
                        factionUser.factionId!!,
                        targetFactionUser.id
                    )
                )

                CoreProvider.Databases.Redis.ECHO.provide().publishToCurrentServer(
                    FactionUserInvitedEchoPacket(
                        factionInvite.factionUserId,
                        factionInvite.factionId,
                        factionInvite.createdAt
                    )
                )

                commandSender.sendMessage(
                    ComponentBuilder()
                        .append("§aVocê convidou o usuário ${targetFactionUser.getFancyName()} §apara sua facção.")
                        .append("\n")
                        .append("§aPara remover o convite clique ")
                        .append("§c§lAQUI")
                        .event(
                            ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/f convite remover ${targetFactionUser.name}"
                            )
                        )
                        .append("§r§a.")
                        .create()
                )
                true
            }
        }
    }

}