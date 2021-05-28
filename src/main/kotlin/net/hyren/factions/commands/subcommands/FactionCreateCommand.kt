package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.*
import net.hyren.core.shared.misc.utils.DefaultMessage
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.*
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.echo.packet.FactionUserUpdatedEchoPacket
import net.hyren.factions.faction.storage.dto.CreateFactionDTO
import net.hyren.factions.user.role.Role
import net.hyren.factions.user.storage.dto.UpdateFactionUserDTO
import net.md_5.bungee.api.chat.*
import org.bukkit.command.CommandSender
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FactionCreateCommand : CustomCommand("criar") {

    override fun getParent() = FactionCommand()

    override fun getUsage0(): Array<BaseComponent> = ComponentBuilder(
        "§cUtilize /${getNameExact()} <tag> <nome>."
    ).create()

    override fun getDescription0() = "Criar uma facção."

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        if (args.size < 2) {
            commandSender.sendMessage(usage)
            return false
        }

        val tag = args[0].uppercase()
        val name = args.copyOfRange(1, args.size).joinToString(" ")

        var factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
            "faction user is null"
        )

        if (factionUser.hasFaction()) {
            commandSender.sendMessage(DefaultMessage.YOU_ALREADY_HAVE_FACTION)
            return false
        }

        if (tag.length != 3) {
            commandSender.sendMessage(
                TextComponent("§cA tag de sua facção deve conter 3 caracteres.")
            )
            return false
        }

        if (!FactionsConstants.Faction.FACTION_TAG_REGEX.matches(tag)) {
            commandSender.sendMessage(
                TextComponent("§cA tag da facção não pode conter caracteres especiais.")
            )
            return false
        }

        if (FactionsProvider.Controllers.FactionController.isFactionTagTaken(tag)) {
            commandSender.sendMessage(
                TextComponent("§cOps! Já existe outra facção utilizando a tag \"$tag\".")
            )
            return false
        }

        if (name.length < 5 || name.length > 20) {
            commandSender.sendMessage(
                TextComponent("§cO nome de sua facção deve conter de 5 a 20 caracteres.")
            )
            return false
        }

        if (!FactionsConstants.Faction.FACTION_NAME_REGEX.matches(name)) {
            commandSender.sendMessage(
                TextComponent("§cO nome de sua facção não pode conter caracteres especiais.")
            )
            return false
        }

        if (FactionsProvider.Controllers.FactionController.isFactionNameTaken(name)) {
            commandSender.sendMessage(
                TextComponent("§cJá existe outra facção utilizando o nome \"$name\".")
            )
            return false
        }

        val faction = FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().create(
            CreateFactionDTO(
                name,
                tag
            )
        ) ?: throw NullPointerException("cannot create faction $name - $tag")

        factionUser = FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
            UpdateFactionUserDTO(
                user.id
            ) {
                it.factionId = faction.id
                it.role = Role.LEADER
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
            TextComponent("§aSua facção foi criada com sucesso!")
        )
        return true
    }

}
