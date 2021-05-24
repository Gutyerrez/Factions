package net.hyren.factions.misc.utils

import com.google.common.collect.Sets
import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.core.shared.misc.utils.ChatColor
import net.hyren.core.spigot.misc.utils.DirectionUtils
import net.hyren.factions.FactionsConstants
import net.hyren.factions.FactionsProvider
import net.hyren.factions.faction.land.data.FactionLand
import net.hyren.factions.faction.land.data.LandType
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.user.data.FactionUser
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
fun FactionUser.drawMap(): Array<BaseComponent> {
    val player = getPlayer()

    if (player == null) {
        return ComponentBuilder().create()
    }

    val location = player.location

    val locationChunkX = location.blockX shr 4
    val locationChunkZ = location.blockZ shr 4

    val minChunkX = locationChunkX - 9
    val minChunkZ = locationChunkZ - 9

    val maxChunkX = locationChunkX + 9
    val maxChunkZ = locationChunkZ + 9

    val lands = Sets.newTreeSet<FactionLand> { o1, o2 ->
        o2.landType.color.code.compareTo(o1.landType.color.code)
    }

    var colors: Array<Array<FactionLand>> = Array<Array<FactionLand>>(19) {
        sizedArray<FactionLand>(19)
    }

    for (x in minChunkX..maxChunkX) {
        for (z in minChunkZ..maxChunkZ) {
            val indexX = x - minChunkX
            val indexY = z - minChunkZ

            var factionLand = FactionsProvider.Cache.Local.FACTION_LANDS.provide().fetchByXAndZ(x, z)

            if (factionLand != null) {
                lands.add(factionLand)

                colors[indexX][indexY] = factionLand

                continue
            }

            factionLand = FactionLand(
                EntityID(
                    UUID.randomUUID(),
                    FactionsTable
                ),
                LandType.FREE,
                Bukkit.getWorlds()[0].name,
                x,
                z,
                DateTime.now(
                    CoreConstants.DATE_TIME_ZONE
                )
            )

            lands.add(factionLand)

            colors[indexX][indexY] = factionLand
        }
    }

    val factionLand = FactionLand(
        EntityID(
            UUID.randomUUID(),
            FactionsTable
        ),
        LandType.CURRENT,
        Bukkit.getWorlds()[0].name,
        locationChunkX,
        locationChunkZ,
        DateTime.now(
            CoreConstants.DATE_TIME_ZONE
        )
    )

    lands.add(factionLand)

    colors[9][9] = factionLand

    val direction = DirectionUtils.yawToFace(location.yaw)
    val vectorDirection = DirectionUtils.vectorToFace(location.direction)

    when (vectorDirection) {
        BlockFace.EAST -> colors = rotateToRight(colors, 1)
        BlockFace.SOUTH -> colors = rotateToRight(colors, 2)
        BlockFace.WEST -> colors = rotateToRight(colors, 3)
    }

    return ComponentBuilder()
        .append("\n")
        .append { componentBuilder, _ ->
            for (y in 0..19) {
                for (x in 0..19) {
                    val factionLand = colors[x][y]

                    componentBuilder.append(ChatColor.BOLD.toString())
                        .append(factionLand.landType.color.toString())
                        .append(FactionsConstants.Symbols.CUBE.toString())
                }

                when (y) {
                    2 -> {
                        componentBuilder.append("${ChatColor.GOLD}")
                            .append("${ChatColor.BOLD}")
                            .append("  ")
                            .append("${if (direction == BlockFace.NORTH_WEST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("\\")
                            .append("${if (direction == BlockFace.NORTH) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("N")
                            .append("${if (direction == BlockFace.NORTH_EAST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("/")
                    }
                    3 -> {
                        componentBuilder.append("${ChatColor.GOLD}")
                            .append("${ChatColor.BOLD}")
                            .append("  ")
                            .append("${if (direction == BlockFace.WEST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("O")
                            .append("${ChatColor.GOLD}${ChatColor.BOLD}+")
                            .append("${if (direction == BlockFace.EAST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("L")
                    }
                    4 -> {
                        componentBuilder.append("${ChatColor.GOLD}")
                            .append("${ChatColor.BOLD}")
                            .append("  ")
                            .append("${if (direction == BlockFace.SOUTH_EAST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("/")
                            .append("${if (direction == BlockFace.SOUTH) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("S")
                            .append("${if (direction == BlockFace.SOUTH_WEST) {
                                ChatColor.RED
                            } else {
                                ChatColor.GOLD
                            }}")
                            .append("\\")
                    }
                    6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 -> {
                        LandType.values().filter { it != LandType.CONTESTED }.forEach {
                            componentBuilder.append("  ")
                                .append("${it.color}")
                                .append(FactionsConstants.Symbols.CUBE.toString())
                                .append("${ChatColor.WHITE} ${it.displayName}")
                        }
                    }
                }

                componentBuilder.append("\n")
            }

            componentBuilder
        }
        .append("\n")
        .create()
}


internal fun rotateToRight(
    array: Array<Array<FactionLand>>,
    times: Int = 0
): Array<Array<FactionLand>> {
    var _array = Array<Array<FactionLand>>(array.size) {
        sizedArray<FactionLand>(array.size)
    }

    for (x in 0..array.size) {
        for (z in 0..array.size) {
            _array[z][(array.size - 1) - x] = array[x][z]
        }
    }

    if (times > 1) {
        return rotateToRight(_array, times - 1)
    }

    return _array
}