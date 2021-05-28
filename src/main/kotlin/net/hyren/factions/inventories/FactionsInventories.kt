package net.hyren.factions.inventories

import net.hyren.core.spigot.CoreSpigotConstants
import net.hyren.core.spigot.inventory.CustomInventory
import net.hyren.core.spigot.misc.utils.*
import net.hyren.factions.FactionsProvider
import net.hyren.factions.user.data.FactionUser
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
final class FactionInventory(
    factionUser: FactionUser
) : CustomInventory(
    if (factionUser.hasFaction()) {
        "${factionUser.getFaction()?.fullyQualifiedName}"
    } else {
        factionUser.name
    },
    if (factionUser.hasFaction()) {
        6 * 9
    } else {
        3 * 9
    }
) {

    init {
        // items header
        setItem(
            10,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skullOwner(factionUser.name)
                .name(factionUser.getHighestGroup().getColoredPrefix() + factionUser.name)
                .lore(
                    arrayOf(
                        "§fPoder: §7${factionUser.getPowerRounded()}/${factionUser.getMaxPowerRounded()}",
                        "§fCoins: §70",
                        "§fCargo: §7${if (factionUser.hasFaction()) {
                            factionUser.role?.prefix + factionUser.role?.displayName
                        } else {
                            "Nenhum."
                        }}",
                        "§fKDR: §7${factionUser.getKDR()}",
                        "§fAbates: §7${factionUser.getTotalKills()} §8[Inimigo: §7${
                            factionUser.enemyKills
                        } §8Neutro: §7${
                            factionUser.neutralKills
                        } §8Civil: §7${
                            factionUser.civilianKills
                        }§8]",
                        "§fMortes: §7${factionUser.getTotalDeaths()} §8[Inimigo: §7${
                            factionUser.enemyDeaths
                        } §8Neutro: §7${
                            factionUser.neutralDeaths
                        } §8Civil: §7${
                            factionUser.civilianDeaths
                        }§8]",
                        "§fGuerras ganhas: §70",
                        "§fStatus: ${if (factionUser.isOnline()) {
                            "§aOnline"
                        } else {
                            "§cOffline"
                        }}"
                    )
                )
                .build()
        )

        setItem(
            13,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skull(
                    BlockColor.MAGENTA
                )
                .name("§eRanking de facções")
                .lore(
                    arrayOf(
                        "§7Clique para ver os ranking com",
                        "§7as melhores facções do servidor."
                    )
                )
                .build()
        )

        setItem(
            14,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skull(
                    BlockColor.BLUE
                )
                .name("§eFacções online")
                .lore(
                    arrayOf(
                        "§7Clique para ver as facções online."
                    )
                )
                .build()
        )

        setItem(
            15,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skull(
                    BlockColor.ORANGE
                )
                .name("§eAssistir Batalhas")
                .lore(
                    arrayOf(
                        "§7Não há batalhas ocorrendo",
                        "§7no momento."
                    )
                )
                .build()
        )

        setItem(
            16,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skull(
                    BlockColor.YELLOW
                )
                .name("§eAjuda")
                .lore(
                    arrayOf(
                        "§7Todas as ações disponíveis neste menu",
                        "§7também podem ser realizadas",
                        "§7por comando. Utilize \"§f/f ajuda§7\"",
                        "§7para ver todos os comandos disponíveis."
                    )
                )
                .build()
        )

        if (factionUser.hasFaction()) {
            // faction items
        } else {
            setItem(
                31,
                ItemBuilder(Material.BANNER)
                    .build()
            )

            setItem(
                32,
                ItemBuilder(Material.EMPTY_MAP)
                    .build()
            )

            // a dirt varia de acordo com a zona atual

            setItem(
                33,
                ItemBuilder(Material.DIRT)
                    .build()
            )

            setItem(
                34,
                ItemBuilder(Material.PAPER)
                    .amount(factionUser.getReceivedInvites().size)
                    .build()
            )

            setItem(
                35,
                ItemBuilder(Material.SKULL_ITEM)
                    .durability(3)
                    .skull(BlockColor.GREY)
                    .build()
            )
        }
    }

}