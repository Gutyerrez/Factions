package net.hyren.factions

import net.hyren.core.shared.CoreProvider
import net.hyren.core.spigot.command.registry.CommandRegistry
import net.hyren.core.spigot.misc.plugin.CustomPlugin
import net.hyren.factions.commands.FactionCommand
import net.hyren.factions.echo.packet.listeners.FactionUserInviteAcceptedEchoPacketListener
import net.hyren.factions.echo.packet.listeners.FactionUserInvitedEchoPacketListener
import net.hyren.factions.echo.packet.listeners.FactionUserUpdatedEchoPacketListener
import net.hyren.factions.listeners.GenericListener
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

        pluginManager.registerEvents(GenericListener(), this)

        /**
         * Echo packets
         */

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserInvitedEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserInviteAcceptedEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(FactionUserUpdatedEchoPacketListener())
    }

}