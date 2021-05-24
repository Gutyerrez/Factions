package net.hyren.factions.commands.restriction

import net.hyren.core.shared.commands.restriction.entities.CommandRestrictable
import net.hyren.core.shared.users.data.User
import net.hyren.factions.FactionsProvider
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author Gutyerrez
 */
interface FactionNeededCommandRestrictable : CommandRestrictable {

    override fun canExecute(user: User?): Boolean {
        if (user == null) {
            return false
        }

        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user.id) ?: return false

        return factionUser.hasFaction()
    }

    override fun getErrorMessage() = ComponentBuilder()
        .append("§cÉ necessário pertencer a uma facção para executar este comando.")
        .create()

}