package net.hyren.factions.inventories

import net.hyren.core.shared.CoreConstants
import net.hyren.core.spigot.inventory.CustomInventory
import net.hyren.core.spigot.misc.utils.*
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.land.data.LandType
import net.hyren.factions.misc.controllers.FactionCreationController
import net.hyren.factions.misc.utils.drawMap
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.dto.UpdateFactionUserDTO
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * @author Gutyerrez
 */
class FactionInventory(
    private val factionUser: FactionUser
) : CustomInventory(
    if (factionUser.hasFaction()) {
        "${factionUser.getFaction()?.fullyQualifiedName}"
    } else {
        factionUser.name
    },
    if (factionUser.hasFaction()) {
        6 * 9
    } else {
        5 * 9
    }
) {

    init {
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
                ).build()
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
                ).build()
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
                ).build()
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
                ).build()
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
                ).build(),
            Consumer {
                val player = it.whoClicked as Player

                player.performCommand("/f ajuda")
            }
        )

        if (factionUser.hasFaction()) {
            // faction items
        } else {
            setItem(
                29,
                ItemBuilder(Material.BANNER)
                    .color(DyeColor.WHITE)
                    .name("§eCriar facção")
                    .lore(
                        arrayOf(
                            "§7Crie sua própria facção!"
                        )
                    ).build(),
                Consumer { FactionCreationController.startNewFactionCreation(factionUser) }
            )

            setButton(ButtonType.MAP)

            setButton(ButtonType.FACTION_INVITES)

            setItem(
                31,
                ItemBuilder(
                    if (FactionsProvider.Cache.Local.FACTION_LANDS.provide().fetchByXAndZ(
                            factionUser.getChunk().x,
                            factionUser.getChunk().z
                    ) == null) {
                        Material.GRASS
                    } else {
                        Material.DIRT
                    }
                ).durability(
                    if (FactionsProvider.Cache.Local.FACTION_LANDS.provide().fetchByXAndZ(
                        factionUser.getChunk().x,
                        factionUser.getChunk().z
                    )?.landType == LandType.WAR) {
                        1
                    } else {
                        0
                    }
                ).build()
            )

            setButton(ButtonType.CHUNKS)
        }
    }

    private fun setButton(
        buttonType: ButtonType
    ) = when (buttonType) {
        ButtonType.MAP -> setItem(
            30,
            ItemBuilder(Material.EMPTY_MAP)
                .name("§aMapa")
                .lore(
                    arrayOf(
                        "§7Veja o mapa com as facções próximas.",
                        "",
                        "§fBotão esquerdo: §7Ver o mapa",
                        "§fBotão direito: §7Ligar modo mapa",
                        "",
                        "§fModo mapa: ${
                            if (factionUser.isMapAutoUpdating) {
                                "§aLigado"
                            } else {
                                "§cDesligado"
                            }
                        }"
                    )
                ).build(),
            Consumer { event ->
                val player = event.whoClicked as Player

                when (event.click) {
                    ClickType.RIGHT -> {
                        if (CoreConstants.COOLDOWNS.inCooldown(factionUser, "change-map-state")) {
                            return@Consumer
                        }

                        factionUser.isMapAutoUpdating = !factionUser.isMapAutoUpdating

                        FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
                            UpdateFactionUserDTO(
                                factionUser.id
                            ) {
                                it.isMapAutoUpdating = factionUser.isMapAutoUpdating
                            }
                        )

                        CoreConstants.COOLDOWNS.start(factionUser, "change-map-state", TimeUnit.SECONDS.toMillis(3))
                    }
                    ClickType.LEFT -> {
                        player.closeInventory()

                        player.sendMessage(factionUser.drawMap())
                    }
                }
            }
        )
        ButtonType.FACTION_INVITES -> setItem(
            32,
            ItemBuilder(Material.PAPER)
                .amount(factionUser.getReceivedInvites().size)
                .name("§aConvites de Facções")
                .lore(
                    arrayOf(
                        if (factionUser.hasInvites()) {
                            "§7Você possui ${factionUser.getReceivedInvites().size} ${
                                if (factionUser.getReceivedInvites().size > 1) {
                                    "convites"
                                } else {
                                    "convite"
                                }
                            }."
                        } else {
                            "§7Você não possui nenhum convite pendente."
                        }
                    )
                ).build()
        )
        ButtonType.CHUNKS -> setItem(
            33,
            ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .skull(
                    if (factionUser.isSeeingChunks) {
                        BlockColor.GREEN
                    } else {
                        BlockColor.GREY
                    }
                )
                .name("§aVer terras")
                .lore(
                    arrayOf(
                        "§7Clique para ver as delimitações das terras",
                        "",
                        "§fVer terras: ${if (factionUser.isSeeingChunks) {
                            "§aLigado"
                        } else {
                            "§cDesligado"
                        }}"
                    )
                ).build(),
            Consumer {
                if (CoreConstants.COOLDOWNS.inCooldown(factionUser, "change-seeing-chunks-state")) {
                    return@Consumer
                }

                factionUser.isSeeingChunks = !factionUser.isSeeingChunks

                FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
                    UpdateFactionUserDTO(
                        factionUser.id
                    ) {
                        it.isSeeingChunks = factionUser.isSeeingChunks
                    }
                )

                CoreConstants.COOLDOWNS.start(factionUser, "change-seeing-chunks-state", TimeUnit.SECONDS.toMillis(3))
            }
        )
    }

    private enum class ButtonType {

        MAP, FACTION_INVITES, CHUNKS;

    }

}