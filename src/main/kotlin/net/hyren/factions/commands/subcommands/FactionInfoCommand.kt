package net.hyren.factions.commands.subcommands

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.users.data.User
import net.hyren.core.spigot.command.CustomCommand
import net.hyren.factions.FactionsConstants
import net.hyren.factions.FactionsProvider
import net.hyren.factions.commands.FactionCommand
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender

/**
 * @author Gutyerrez
 */
class FactionInfoCommand : CustomCommand("info") {

    override fun getParent() = FactionCommand()

    override fun getUsage0(): Array<BaseComponent> = ComponentBuilder(
        "§cUtilize /${this.getNameExact()} <tag>."
    ).create()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val faction = if (args.size == 1) {
            FactionsProvider.Cache.Local.FACTION.provide().fetchByTag(args[0])
        } else {
            FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user!!.id)?.faction ?: commandSender.sendMessage(
                usage
            )
            return false
        }

        return if (faction != null) {
            commandSender.sendMessage(
                ComponentBuilder()
                    .append("                    §e${faction.tag} - ${faction.name}")
                    .append("\n\n")
                    .append(if (user!!.hasGroup(Group.MANAGER)) {
                        "§2ID da facção: §7${faction.id.value}"
                    } else "")
                    .append("\n")
                    .append("§2Terras/Poder/Poder máximo: §7${
                        faction.getLands().size
                    }/${
                        faction.powerRounded
                    }/${
                        faction.maxPowerRounded
                    }")
                    .append("\n")
                    .append("§2Líder: §7${faction.getLeader().name}")
                    .append("\n")
                    .append("§2Membros: §7(${faction.getUsersCount()}/${faction.maxUsers}) ${faction.getOnlineUsersCount()} ${
                        if (faction.getOnlineUsersCount() > 1) "onlines" else "online"
                    }")
                    .append("\n")
                    .append { componentBuilder, _ ->
                        faction.getUsers().sortedBy {
                            it.isOnline()
                        }.forEachIndexed { index, factionUser ->
                            componentBuilder.append("${
                                if (factionUser.isOnline()) "§a" else "§7"
                            }${FactionsConstants.Symbols.BLACK_CIRCLE} ${factionUser.role?.prefix}${factionUser.name}")

                            if (index % 3 == 0) {
                                componentBuilder.append("\n")
                            }
                        }

                        componentBuilder
                    }
                    .append("\n")
                    .append("§2KDR: §7${faction.getKDR()}")
                    .append("\n")
                    .append("§2Abates: §7${faction.getTotalKills()} §8[Inimigo: §7${
                        faction.getEnemyKills()
                    } §8Neutro: §7${
                        faction.getNeutralKills()
                    } §8Civil: §7${
                        faction.getCivilianKills()
                    }§8]")
                    .append("\n")
                    .append("§2Mortes: §7${faction.getTotalDeaths()} §8[Inimigo: §7${
                        faction.getEnemyDeaths()
                    } §8Neutro: §7${
                        faction.getNeutralDeaths()
                    } §8Civil: §7${
                        faction.getCivilianDeaths()
                    }§8]")
                    .append("\n")
                    .append("§2${
                        if (FactionsConstants.Faction.MAX_ALLIES > 1) "Aliados" else "Aliado"
                    }: §7Nenhum.")
                    .append("\n")
                    .append("§2Inimigos: §7Nenhum.")
                    .append("\n")
                    .append("§2Guerras ganhas: §7${faction.warWins}")
                    .create()
            )
            true
        } else {
            commandSender.sendMessage(usage)
            false
        }
    }

}