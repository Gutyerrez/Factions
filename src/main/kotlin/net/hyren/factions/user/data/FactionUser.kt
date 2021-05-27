package net.hyren.factions.user.data

import net.hyren.core.shared.*
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.users.data.User
import net.hyren.factions.*
import net.hyren.factions.alpha.misc.player.list.data.PlayerList
import net.hyren.factions.user.role.Role
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.math.RoundingMode
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Gutyerrez
 */
data class FactionUser(
    val user: User,

    // Current faction role if don't have a faction this can be null

    var role: Role? = null,

    // Current faction if don't have a faction this can be null

    var factionId: EntityID<UUID>? = null,

    // Power

    var power: Double = 0.0,
    var maxPower: Double = 0.0,

    val powerRounded: Int = power.roundToInt(),
    val maxPowerRounded: Int = maxPower.roundToInt(),

    // Kills by default = 0

    var enemyKills: Int = 0,
    var neutralKills: Int = 0,
    var civilianKills: Int = 0,

    // Deaths by default = 0

    var enemyDeaths: Int = 0,
    var neutralDeaths: Int = 0,
    var civilianDeaths: Int = 0,

    // Settings

    var mapAutoUpdating: Boolean = false,
    var seeingChunks: Boolean = false,

    // Timestamps

    override var createdAt: DateTime = DateTime.now(
        CoreConstants.DATE_TIME_ZONE
    ),

    override var updatedAt: DateTime? = null
) : User(
    user.id,
    user.name,
    user.email,
    user.discordId,
    user.twoFactorAuthenticationEnabled,
    user.twoFactorAuthenticationCode,
    user.twitterAccessToken,
    user.twitterTokenSecret,
    user.lastAddress,
    user.lastLobbyName,
    user.lastLoginAt,
    user.createdAt,
    user.updatedAt
) {

    // Player List
    lateinit var playerList: PlayerList

    fun initPlayerList(player: Player) {
        playerList = PlayerList(player)

        updatePlayerList()
    }

    fun updatePlayerList() {
        if (!this::playerList.isInitialized) {
            throw UninitializedPropertyAccessException("PlayerList variable is not initialized")
        }

        // 0 - 19

        playerList.update(0, "§e§lMINHA FACÇÃO")

        if (hasFaction()) {
            playerList.update(1, "§e[${getFactionTag()}] ${getFactionName()}")
            playerList.update(2, "§1")

            var index = 3

            getFaction()?.getUsers()?.forEachIndexed { _index, factionUser ->
                index = index + _index

                playerList.update(index, "${
                    if (factionUser.isOnline()) {
                        "§a"
                    } else {
                        "§7"
                    }
                } ${FactionsConstants.Symbols.BLACK_CIRCLE} ${
                    factionUser.getHighestGroup(CoreProvider.application.server).getColoredPrefix() 
                }${factionUser.role?.prefix + factionUser.name}")
            }

            do {
                playerList.update(index, "§1")

                index++
            } while (index != 19)

            // 20 - 39

            playerList.update(20, "§e§lALIANÇA")
            playerList.update(21, "§eSem ${
                if (FactionsConstants.Faction.MAX_ALLIES > 1) {
                    "alidos"
                } else {
                    "alido"
                }
            }.")
            playerList.update(22, "§1")
            playerList.update(23, "§eUse §f/f aliança §epara")
            playerList.update(24, "§econvidar outra facção")
            playerList.update(25, "§ede confiança para ser")
            playerList.update(26, "§ea sua aliada.")
            playerList.update(27, "§0")
            playerList.update(28, "§eGerencie as permissões")
            playerList.update(29, "§eda sua facção aliada")
            playerList.update(30, "§eusando o comando")
            playerList.update(31, "§f/f permissões.")

            index = 32

            do {
                playerList.update(index, "§1")

                index++
            } while (index != 39)

            // allies
        } else {
            // 40 - 59
            playerList.update(40, "§e§lSTAFF ONLINE")

            var index = 42

            CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByServer(
                CoreProvider.application.server!!
            ).map { CoreProvider.Cache.Local.USERS.provide().fetchById(it) }.filter {
                it != null && it.hasGroup(Group.HELPER)
            }.forEachIndexed { _index, user ->
                index = index + _index

                playerList.update(index, user?.getHighestGroup()?.getColoredPrefix() + user?.name)
            }

            do {
                playerList.update(index, "§1")

                index++
            } while (index != 59)
        }

        // later

        // 60 - 79

        playerList.update(60, "§e§lMINHAS INFORMAÇÕES")
        playerList.update(62, "§fCoins: §a0.00")
        playerList.update(63, "§fCash: §a0.00")
        playerList.update(64, "§fPoder: §a$powerRounded")
        playerList.update(65, "§fKDR: §a${getKDR()}")
        playerList.update(67, "§eHabilidades:")
        playerList.update(68, "§f Acrobacia: §a0 §7(0/1020)")
        playerList.update(69, "§f Alquimia: §a0 §7(0/1020)")
        playerList.update(70, "§f Arqueiro: §a0 §7(0/1020)")
        playerList.update(71, "§f Machado: §a0 §7(0/1020)")
        playerList.update(72, "§f Escavação: §a0 §7(0/1020)")
        playerList.update(73, "§f Herbalismo: §a0 §7(0/1020)")
        playerList.update(74, "§f Mineração: §a0 §7(0/1020)")
        playerList.update(75, "§f Reparação: §a0 §7(0/1020)")
        playerList.update(76, "§f Espadas: §a0 §7(0/1020)")

        var index = 77

        do {
            playerList.update(index, "§1")

            index++
        } while (index != 79)
    }

    fun getFaction() = if (factionId != null) {
        FactionsProvider.Cache.Local.FACTION.provide().fetchById(factionId!!)
    } else {
        null
    }

    fun getFactionName() = getFaction()?.name

    fun getFactionTag() = getFaction()?.tag

    fun getReceivedInvites() = FactionsProvider.Cache.Local.FACTION_INVITES.provide().fetchByFactionUserId(id)

    fun getPlayer(): Player? = Bukkit.getPlayer(getUniqueId())

    fun getTotalKills() = enemyKills + civilianDeaths + neutralKills

    fun getTotalDeaths() = enemyDeaths + civilianDeaths + neutralDeaths

    fun getKDR() = (getTotalKills() / if (getTotalDeaths() == 0) {
        1
    } else { getTotalDeaths() }).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

    fun hasFaction() = factionId != null

    override fun isOnline(): Boolean = super.isOnline() && getConnectedBukkitApplication()?.server == CoreProvider.application.server

}
