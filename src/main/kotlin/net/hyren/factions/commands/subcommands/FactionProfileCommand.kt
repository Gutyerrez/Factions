package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.utils.DateFormatter
import net.hyren.core.shared.misc.utils.DefaultMessage
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.FactionCommand
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FactionProfileCommand : CustomCommand("perfil") {

    override fun getParent() = FactionCommand()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val factionUser = if (args.size == 1) {
            val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserName(args[0])

            if (factionUser == null) {
                commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
                return false
            }

            factionUser
        } else {
            FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id) ?: throw NullPointerException(
                "faction user is null"
            )
        }

        commandSender.sendMessage(
            ComponentBuilder()
                .append("\n")
                .append("                    ${factionUser.getHighestGroup(CoreProvider.application.server).prefix}§e${
                    if (factionUser.hasFaction()) {
                        factionUser.role?.prefix + factionUser.factionTag + " " + factionUser.name
                    } else {
                        factionUser.name
                    }
                }")
                .append("\n\n")
                .append { componentBuilder, _ ->
                    if (user!!.hasGroup(Group.MANAGER)) {
                        componentBuilder.append("§fID do usuário: §7${factionUser.getUniqueId()}")
                    }

                    componentBuilder
                }
                .append("\n")
                .append("§fPoder: §7${factionUser.powerRounded}/${factionUser.maxPowerRounded}")
                .append("\n")
                .append("§fFacção: §7${
                    if (factionUser.hasFaction()) {
                        factionUser.factionName
                    } else {
                        "Nenhuma."
                    }
                }")
                .append("\n")
                .append("§fCoins: §70")
                .append("\n")
                .append("§fCargo: §7${
                    if (factionUser.hasFaction()) {
                        factionUser.role?.displayName
                    } else {
                        "Nenhum."
                    }
                }")
                .append("\n")
                .append("§fKDR: §7${factionUser.getKDR()}")
                .append("\n")
                .append("§fAbates: §7${factionUser.getTotalKills()} §8[Inimigo: §7${
                    factionUser.enemyKills
                } §8Neutro: §7${
                    factionUser.neutralKills
                } §8Civil: §7${
                    factionUser.civilianKills
                }§8]")
                .append("\n")
                .append("§fMortes: §7${factionUser.getTotalDeaths()} §8[Inimigo: §7${
                    factionUser.enemyDeaths
                } §8Neutro: §7${
                    factionUser.neutralDeaths
                } §8Civil: §7${
                    factionUser.civilianDeaths
                }§8]")
                .append("\n")
                .append("§fStatus: ${
                    if (factionUser.isOnline() && factionUser.getConnectedBukkitApplication()?.server == CoreProvider.application.server) {
                        "§aOnline"
                    } else {
                        "§cOffline"
                    }
                }")
                .append("\n")
                .append("§fÚltimo login: §7${DateFormatter.formatToDefault(
                    factionUser.updatedAt ?: DateTime.now(
                        CoreConstants.DATE_TIME_ZONE
                    ),
                    "às"
                )}")
                .append("\n")
                .create()
        )
        return true
    }

}