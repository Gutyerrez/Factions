package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreProviderimport net.hyren.core.shared.commands.argument.Argumentimport net.hyren.core.shared.misc.utils.DefaultMessageimport net.hyren.core.shared.users.data.Userimport net.hyren.core.spigot.command.CustomCommandimport net.hyren.factions.ALREADY_HAVE_FACTIONimport net.hyren.factions.FactionsProviderimport net.hyren.factions.commands.FactionCommandimport net.hyren.factions.commands.restriction.FactionNeededCommandRestrictableimport net.hyren.factions.echo.packet.FactionUserInvitedEchoPacketimport net.hyren.factions.faction.invite.storage.dto.CreateFactionInviteDTOimport net.md_5.bungee.api.chat.ClickEventimport net.md_5.bungee.api.chat.ComponentBuilderimport net.md_5.bungee.api.chat.TextComponentimport org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionInviteAddCommand : CustomCommand("convidar"), FactionNeededCommandRestrictable {

    override fun getParent() = FactionCommand()

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id)!!
        val targetFactionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserName(args[0])

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
                val factionInvite = FactionsProvider.Repositories.PostgreSQL.FACTIONS_INVITES_REPOSITORY.provide().create(
                    CreateFactionInviteDTO(
                        targetFactionUser.id,
                        factionUser.factionId!!
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
