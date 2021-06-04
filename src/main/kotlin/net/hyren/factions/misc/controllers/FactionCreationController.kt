package net.hyren.factions.misc.controllers

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.*
import net.hyren.factions.*
import net.hyren.factions.echo.packet.FactionUserUpdatedEchoPacket
import net.hyren.factions.faction.storage.dto.CreateFactionDTO
import net.hyren.factions.user.data.FactionUser
import net.hyren.factions.user.role.Role
import net.hyren.factions.user.storage.dto.UpdateFactionUserDTO
import net.md_5.bungee.api.chat.*
import org.bukkit.entity.Player
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
object FactionCreationController {

    private val CANCEL_MESSAGE = "§7Caso queira cancelar, responda \"cancelar\"."

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build<FactionUser, CreateFactionSteps>()

    fun startNewFactionCreation(
        factionUser: FactionUser
    ) {
        val createFactionSteps = CreateFactionSteps()

        CACHE.put(factionUser, createFactionSteps)

        factionUser.getPlayer()?.sendMessage(
            ComponentBuilder()
                .append("\n")
                .append("§aQual será o nome da sua Facção?")
                .append("\n")
                .append(CANCEL_MESSAGE)
                .append("\n\n")
                .create()
        )
    }

    fun isNewFactionCreating(
        factionUser: FactionUser
    ) = CACHE.getIfPresent(factionUser) != null

    fun getCurrentStep(
        factionUser: FactionUser
    ) = CACHE.getIfPresent(factionUser)!!

    internal fun remove(
        factionUser: FactionUser
    ) = CACHE.invalidate(factionUser)

    class CreateFactionSteps {

        private lateinit var tag: String
        private lateinit var name: String

        fun setUpNewFactionTag(
            player: Player,
            newFactionTag: String
        ): Boolean {
            if (newFactionTag.length != 3) {
                player.sendMessage(TextComponent("§cA tag de sua facção deve conter 3 caracteres."))
                return false
            }

            if (!FactionsConstants.Faction.FACTION_TAG_REGEX.matches(newFactionTag)) {
                player.sendMessage(TextComponent("§cA tag da facção não pode conter caracteres especiais."))
                return false
            }

            if (FactionsProvider.Controllers.FactionController.isFactionTagTaken(newFactionTag)) {
                player.sendMessage(TextComponent("§cOps! Já existe outra facção utilizando a tag \"$newFactionTag\"."))
                return false
            }

            this@CreateFactionSteps.tag = newFactionTag

            return true
        }

        fun setUpNewFactionName(
            player: Player,
            newFactionName: String
        ): Boolean {
            if (newFactionName.length < 5 || newFactionName.length > 20) {
                player.sendMessage(
                    TextComponent("§cO nome de sua facção deve conter de 5 a 20 caracteres.")
                )
                return false
            }

            if (!FactionsConstants.Faction.FACTION_NAME_REGEX.matches(newFactionName)) {
                player.sendMessage(
                    TextComponent("§cO nome de sua facção não pode conter caracteres especiais.")
                )
                return false
            }

            if (FactionsProvider.Controllers.FactionController.isFactionNameTaken(newFactionName)) {
                player.sendMessage(
                    TextComponent("§cJá existe outra facção utilizando o nome \"$newFactionName\".")
                )
                return false
            }

            this@CreateFactionSteps.name = newFactionName

            return true
        }

        fun accept(
            player: Player
        ) {
            remove(getFactionUser(player.uniqueId))

            val faction = FactionsProvider.Repositories.PostgreSQL.FACTIONS_REPOSITORY.provide().create(
                CreateFactionDTO(
                    name,
                    tag
                )
            ) ?: throw NullPointerException("cannot create faction $name - $tag")

            val factionUser = FactionsProvider.Repositories.PostgreSQL.FACTIONS_USERS_REPOSITORY.provide().update(
                UpdateFactionUserDTO(
                    getFactionUser(player.uniqueId).id
                ) {
                    it.factionId = faction.id
                    it.role = Role.LEADER
                    it.updatedAt = DateTime.now(
                        CoreConstants.DATE_TIME_ZONE
                    )
                }
            )

            CoreProvider.Databases.Redis.ECHO.provide().publishToCurrentServer(
                FactionUserUpdatedEchoPacket(
                    factionUser.id
                )
            )

            player.sendMessage(TextComponent("§aYAY! Sua facção foi criada com sucesso!"))
        }

        fun cancel(
            player: Player
        ) {
            remove(getFactionUser(player.uniqueId))

            player.sendMessage(TextComponent("§cA criação da facção foi cancelada."))
        }

        private fun getFactionUser(
            uniqueId: UUID
        ) = FactionsProvider.Cache.Local.FACTION_USER.provide().fetchByUserId(
            uniqueId
        )!!

        fun getCurrentStep() = if (!this::tag.isInitialized) {
            CurrentStep.SET_UP_TAG
        } else if (!this::name.isInitialized) {
            CurrentStep.SET_UP_NAME
        } else if (this::tag.isInitialized && this::name.isInitialized) {
            CurrentStep.CONFIRM_CREATION
        } else throw IllegalStateException("Cannot fetch current step for creation of faction $tag - $name")

        enum class CurrentStep {

            SET_UP_TAG, SET_UP_NAME, CONFIRM_CREATION;

        }

    }
}