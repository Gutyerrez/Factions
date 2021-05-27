package net.hyren.factions.misc.tab.list

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.groups.Group
import net.hyren.factions.FactionsConstants
import net.hyren.factions.alpha.misc.player.list.data.PlayerList
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.storage.table.FactionsUsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Gutyerrez
 */
object TabListManager {

    private val CACHE = Caffeine.newBuilder()
        .build<EntityID<UUID>, PlayerList>()

    fun fetchByUserId(userId: EntityID<UUID>) = CACHE.getIfPresent(userId)

    fun fetchByUserId(userId: UUID) = CACHE.getIfPresent(
        EntityID(
            userId,
            FactionsUsersTable
        )
    )

    fun create(
        userId: EntityID<UUID>,
        playerList: PlayerList
    ) = CACHE.put(
        userId,
        playerList
    )

}

fun FactionUser.updatePlayerList() {
    val player = getPlayer() ?: return

    println("Update")

    if (TabListManager.fetchByUserId(id) == null) {
        TabListManager.create(id, PlayerList(player))
    }

    val playerList = TabListManager.fetchByUserId(id)!!

    var index by Delegates.notNull<Int>()

    playerList.update(0, "§e§lMINHA FACÇÃO")

    if (hasFaction()) {
        val faction = getFaction()!!

        playerList.update(1, "§e[${faction.tag}] ${faction.name}")
        playerList.update(2, "§0")

        index = 3

        faction.getUsers().forEach { it ->
            playerList.update(index, "${
                if (it.isOnline()) {
                    "§a"
                } else {
                    "§7"
                }
            } ${FactionsConstants.Symbols.BLACK_CIRCLE} ${
                it.getHighestGroup(CoreProvider.application.server).getColoredPrefix()
            }${it.role?.prefix + it.name}")

            index++
        }

        do {
            playerList.update(index, "§1")

            index++
        } while (index != 20)

        if (!faction.hasAllies()) {
            playerList.update(20, "§e§lALIANÇA")
            playerList.update(
                21, "§eSem ${
                if (FactionsConstants.Faction.MAX_ALLIES > 1) {
                    "alidos"
                } else {
                    "alido"
                }
            }."
            )
            playerList.update(22, "§0")
            playerList.update(23, "§eUse §f/f aliança §epara")
            playerList.update(24, "§econvidar outra facção")
            playerList.update(25, "§ede confiança para ser")
            playerList.update(26, "§ea sua aliada.")
            playerList.update(27, "§0")
            playerList.update(28, "§eGerencie as permissões")
            playerList.update(29, "§eda sua facção aliada")
            playerList.update(30, "§eusando o comando")
            playerList.update(31, "§f/f permissões§e.")

            index = 32

            do {
                playerList.update(index, "§1")

                index++
            } while (index != 40)
        }

        playerList.update(40, "§e§lINFORMAÇÕES ${getFactionTag()}")
        playerList.update(41, "§0")
        playerList.update(42, "§fTerras: §a${faction.getLandCount()}")
        playerList.update(43, "§fMembros: §a${faction.getUsersCount()}/${faction.maxUsers}")
        playerList.update(44, "§fPoder: §a${faction.getPowerRounded()}/${faction.getMaxPowerRounded()}")
        playerList.update(45, "§fKDR: §a${faction.getKDR()}")
        playerList.update(46, "Líder: §a${faction.getLeader().name}")
        playerList.update(47, "§0")
        playerList.update(48, "§a${FactionsConstants.Symbols.TINY_TRIANGLE_UP} Abates:")
        playerList.update(49, "§f Civil: §7${faction.getCivilianKills()}")
        playerList.update(50, "§f Neutro: §7${faction.getNeutralKills()}")
        playerList.update(51, "§f Inimigo: §7${faction.getEnemyKills()}")
        playerList.update(52, "§0")
        playerList.update(53, "§c${FactionsConstants.Symbols.TINY_TRIANGLE_DOWN} Mortes:")
        playerList.update(54, "§f Civil: §7${faction.getCivilianDeaths()}")
        playerList.update(55, "§f Neutro: §7${faction.getNeutralDeaths()}")
        playerList.update(56, "§f Inimigo: §7${faction.getEnemyDeaths()}")
        playerList.update(57, "§0")
        playerList.update(58, "§0")
        playerList.update(59, "§0")
    } else {
        playerList.update(1, "§eSem facção.")
        playerList.update(2, "§0")
        playerList.update(3, "§eUse §f/f criar <tag> <nome>")
        playerList.update(4, "§ee crie uma nova facção,")
        playerList.update(5, "§econstrua sua base,")
        playerList.update(6, "§ee realize suas próprias")
        playerList.update(7, "§einvasões.")
        playerList.update(8, "§0")
        playerList.update(9, "§eGerencie sua facção")
        playerList.update(10, "§eatravés do menu de facção")
        playerList.update(11, "§eusando o comando §f/f menu§e.")

        index = 12

        do {
            playerList.update(index, "§1")

            index++
        } while (index != 40)

        playerList.update(40, "§e§lSTAFF ONLINE")
        playerList.update(41, "§0")

        index = 42

        CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByServer(
            CoreProvider.application.server!!
        ).map { CoreProvider.Cache.Local.USERS.provide().fetchById(it) }.filter {
            it != null && it.hasGroup(Group.HELPER)
        }.forEach {
            playerList.update(
                index,
                it?.getHighestGroup()?.getColoredPrefix() + it?.name
            )

            index++
        }

        do {
            playerList.update(index, "§1")

            index++
        } while (index != 60)
    }

    playerList.update(60, "§e§lMINHAS INFORMAÇÕES")
    playerList.update(61, "§0")
    playerList.update(62, "§fCoins: §a0.00")
    playerList.update(63, "§fCash: §a0.00")
    playerList.update(64, "§fPoder: §a${getPowerRounded()}/${getMaxPowerRounded()}")
    playerList.update(65, "§fXP: §a0")
    playerList.update(66, "§fKDR: §a${getKDR()}")
    playerList.update(67, "§0")
    playerList.update(68, "§eHabilidades:")
    playerList.update(69, "§f Acrobacia: §a0 §7(0/1020)")
    playerList.update(70, "§f Alquimia: §a0 §7(0/1020)")
    playerList.update(71, "§f Arqueiro: §a0 §7(0/1020)")
    playerList.update(72, "§f Machado: §a0 §7(0/1020)")
    playerList.update(73, "§f Escavação: §a0 §7(0/1020)")
    playerList.update(74, "§f Herbalismo: §a0 §7(0/1020)")
    playerList.update(75, "§f Mineração: §a0 §7(0/1020)")
    playerList.update(76, "§f Reparação: §a0 §7(0/1020)")
    playerList.update(77, "§f Espadas: §a0 §7(0/1020)")
    playerList.update(78, "§0")
    playerList.update(79, "§0")
}