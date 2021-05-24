package net.hyren.factions

import net.hyren.core.shared.misc.utils.DefaultMessage
import net.hyren.factions.faction.storage.table.FactionsTable
import net.hyren.factions.misc.utils.ChunkCuboid
import net.hyren.factions.user.role.Role
import net.md_5.bungee.api.chat.TextComponent
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author Gutyerrez
 */
object FactionsConstants {

    object Symbols {

        const val BLACK_CIRCLE = '\u25CF'
        const val CUBE = '\u2588'

    }

    object Faction {

        val WAR_ZONE_ID = EntityID(
            UUID.randomUUID(),
            FactionsTable
        )

        const val MAX_USERS = 15

        const val MAX_ALLIES = 1

        val FACTION_TAG_REGEX = Regex("[a-zA-Z0-9]*")
        val FACTION_NAME_REGEX = Regex("[a-zA-Z0-9].*")

        val DEFAULT_ROLE = Role.RECRUIT

        val WAR_ZONE_CUBOID = ChunkCuboid(
            -8,
            -8,
            8,
            8
        )

    }

    object FactionUser {

        const val DEFAULT_FACTION_USER_POWER = 0.0
        const val DEFAULT_FACTION_USER_MAX_POWER = 5.0

    }

}

val DefaultMessage.YOU_ALREADY_HAVE_FACTION get() = TextComponent("§cVocê já pertence a uma facção.")

val DefaultMessage.ALREADY_HAVE_FACTION get() = TextComponent("§cEste usuário já pertence a uma facção.")

val DefaultMessage.FACTION_IS_NECESSARY_TO_EXECUTE_THIS_COMMAND get() = TextComponent("§cÉ necessário pertencer a uma facção para executar este comando.")

val DefaultMessage.FACTION_NOT_FOUND get() = TextComponent("§cEsta facção não existe.")