package net.hyren.factions.user.role

import java.util.*

/**
 * @author Gutyerrez
 */
enum class Role(
    val prefix: String,
    val displayName: String
) {

    LEADER("#", "Líder"),
    OFFICER("*", "Capitão"),
    MEMBER("+", "Membro"),
    RECRUIT("-", "Recruta");

    val sampleName = displayName.lowercase(Locale.getDefault())

}