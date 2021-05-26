package net.hyren.factions

import net.hyren.core.shared.CoreProvider
import net.hyren.core.spigot.command.registry.CommandRegistry
import net.hyren.core.spigot.misc.plugin.CustomPlugin
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.echo.packet.listeners.*
import net.hyren.factions.listeners.connection.*
import org.bukkit.Bukkit

/**
 * @author Gutyerrez
 */
class FactionsPlugin : CustomPlugin() {

    override fun onEnable() {
        FactionsProvider.prepare()

        val pluginManager = Bukkit.getServer().pluginManager

        /**
         * Commands
         */

        CommandRegistry.registerCommand(FactionCommand())

        /**
         * Bukkit listeners
         */

        pluginManager.registerEvents(PlayerPreLoginListener(), this)
        pluginManager.registerEvents(PlayerJoinListener(), this)

        /**
         * Echo packets
         */

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserInvitedEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserInviteAcceptedEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserUpdateEchoPacketListener())
    }

}