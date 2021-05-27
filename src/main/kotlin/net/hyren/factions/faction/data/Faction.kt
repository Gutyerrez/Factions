package net.hyren.factions.faction.data

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.world.location.SerializedLocation
import net.hyren.factions.*
import net.hyren.factions.user.data.FactionUser
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
data class Faction(
    val id: EntityID<UUID>,
    val name: String,
    val tag: String,
    var warWins: Int = 0,
    var base: SerializedLocation? = null,
    var outPosting: Boolean = false,
    val createdAt: DateTime,
    var updatedAt: DateTime? = null
) {

    var maxUsers = FactionsConstants.Faction.MAX_USERS

    val fullyQualifiedName = "[$tag] - $name"

    fun getPower() = getUsers().sumOf { it.power }

    fun getMaxPower() = getUsers().sumOf { it.maxPower }

    fun getPowerRounded() = getPower().roundToInt()

    fun getMaxPowerRounded() = getMaxPower().roundToInt()

    fun getLeader() = getUsers().first { it.role == Role.LEADER }

    fun getUsers() = FactionsProvider.Cache.Local.FACTION_USERS.provide().fetchByFactionId(id)!!

    fun getUsersCount() = getUsers().size

    fun getOnlineUsers() = getUsers().filter {
        it.isOnline() && it.getConnectedBukkitApplication()?.server == CoreProvider.application.server
    }.toTypedArray()

    fun getOnlineUsersCount() = getOnlineUsers().size

    // this only return player in instance, if player is not in this instance, ignore the player
    fun getOnlinePlayers() = getOnlineUsers().map {
        Bukkit.getPlayer(it.getUniqueId())
    }.stream().filter {
        it != null
    }.toArray() as Array<Player>

    fun getLands() = FactionsProvider.Cache.Local.FACTION_LANDS.provide().fetchByFactionId(id)!!

    fun getLandCount() = getLands().size

    fun getSentInvites() = FactionsProvider.Cache.Local.FACTION_INVITES.provide().fetchByFactionId(id)!!

    fun getEnemyKills() = getUsers().sumOf { it.enemyKills }

    fun getNeutralKills() = getUsers().sumOf { it.neutralKills }

    fun getCivilianKills() = getUsers().sumOf { it.civilianKills }

    fun getTotalKills() = getUsers().sumOf {
        it.enemyKills + it.neutralKills + it.civilianKills
    }

    fun getEnemyDeaths() = getUsers().sumOf { it.enemyDeaths }

    fun getNeutralDeaths() = getUsers().sumOf { it.neutralDeaths }

    fun getCivilianDeaths() = getUsers().sumOf { it.civilianDeaths }

    fun getTotalDeaths() = getUsers().sumOf {
        it.enemyDeaths + it.neutralDeaths + it.civilianDeaths
    }

    fun getKDR()  = (getTotalKills() / if (getTotalDeaths() == 0) {
        1
    } else {
        getTotalDeaths()
    }).toBigDecimal().setScale(2, RoundingMode.UP).toPlainString().replace('.', ',')

    fun hasInvited(factionUser: FactionUser) = getSentInvites().any { it.factionUserId == factionUser.id }

    fun hasAllies() = false

    fun hasEnemies() = false

    override fun hashCode() = id.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as Faction

        if (id != other.id) return false

        return true
    }

}
