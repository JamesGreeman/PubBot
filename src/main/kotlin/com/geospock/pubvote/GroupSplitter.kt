package com.geospock.pubvote

import PubVote.Group
import com.geospock.pubvote.people.People
import com.geospock.pubvote.people.Role
import java.util.Collections

/**
 * @author James
 */
class GroupSplitter(private val maxGroupSize: Int) {

    fun splitGroups(attending: Group): List<Group> {
        val groups = (1..(attending.size + maxGroupSize - 1) / maxGroupSize)
                .map { mutableListOf<People>() }

        Collections.shuffle(attending)

        with(groups) {
            addFromRole(attending, Role.EXECUTIVE_LEADERSHIP_TEAM)
            addFromRole(attending, Role.MANAGEMENT)
            addFromRole(attending, Role.PEOPLE_EARTHLINGS_OTHERS_NICOLA)
        }
        return groups
    }

    private fun List<MutableList<People>>.addFromRole(attending: Group, role: Role) = attending
            .filter { person -> person.role == role }
            .forEach { person ->
                this.minBy { it.size }?.add(person)
                        ?: throw IllegalArgumentException("Needed a group to add to but did not find one.")
            }
}