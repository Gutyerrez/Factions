package net.hyren.factions.listeners.connection

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import java.util.*

/**
 * @author Gutyerrez
 */
class PlayerPreLoginListener : Listener {

    @EventHandler
    fun on(
        event: AsyncPlayerPreLoginEvent
    ) {
        val name = event.name
        val uniqueId = event.uniqueId ?: UUID.nameUUIDFromBytes(
            "OfflinePlayer:$name".toByteArray()
        )

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(uniqueId)

        if (user == null) {
            event.kickMessage = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(
                ComponentBuilder()
                    .append(CoreConstants.Info.COLORED_SERVER_NAME)
                    .append("\n\n")
                    .append("§cNão foi possível carregar o seu usuário, tente novamente mais tarde.")
                    .create()
            )).text
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            return
        }
    }

}