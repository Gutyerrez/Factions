package net.hyren.factions.user.data

import net.hyren.core.shared.*
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
        if (!this::playerList.isInitialized) return

        if (hasFaction()) {

            playerList.update(0, "§e§lMINHA FACÇÃO")
            playerList.update(1, "§e[${getFactionTag()}] ${getFactionName()}")
            playerList.update(2, "§1§2§3§4§5§6§7§8")

            var index = 3

            getFaction()?.getUsers()?.forEach {
                playerList.update(index, "${
                    if (it.isOnline()) {
                        "§a"
                    } else {
                        "§7"
                    }
                } ${FactionsConstants.Symbols.BLACK_CIRCLE} ${it.role?.prefix + it.name}")

                index++
            }

            do {
                playerList.update(index, "§1")

                index++
            } while (index != 80)
        }
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