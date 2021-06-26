package net.hyren.factions.listeners

import net.hyren.core.shared.CoreProvider
import net.hyren.factions.FactionsProvider
import net.hyren.factions.misc.controllers.FactionCreationController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * @author Gutyerrez
 */
class GenericListener : Listener {

    @EventHandler
    fun on(
        event: AsyncPlayerChatEvent
    ) {
        val player = event.player
        val message = event.message

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(player.uniqueId)!!
        val factionUser = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(user.id)!!

        if (FactionCreationController.isNewFactionCreating(factionUser)) {
            val factionCreationSteps = FactionCreationController.getCurrentStep(factionUser)

            when (factionCreationSteps.getCurrentStep()) {
                FactionCreationController.CreateFactionSteps.CurrentStep.SET_UP_TAG -> factionCreationSteps.setUpNewFactionTag(
                    player,
                    message
                )
                FactionCreationController.CreateFactionSteps.CurrentStep.SET_UP_NAME -> factionCreationSteps.setUpNewFactionName(
                    player,
                    message
                )
                FactionCreationController.CreateFactionSteps.CurrentStep.CONFIRM_CREATION -> {
                    if (message.matches(Regex("sim", RegexOption.IGNORE_CASE))) {
                        factionCreationSteps.accept(player)
                    } else if (message.matches(Regex("não", RegexOption.IGNORE_CASE))) {
                        factionCreationSteps.cancel(player)
                    }
                }
            }
        }
    }

}