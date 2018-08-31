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
        val groups = (1 .. (attending.size + maxGroupSize - 1) / maxGroupSize)
                .map { mutableListOf<People>() }

        groups.addFromRole(attending, Role.EXECUTIVE_LEADERSHIP_TEAM)
        groups.addFromRole(attending, Role.COMMERCIAL_SENIOR_MANAGEMENT_TEAM)
        groups.addFromRole(attending, Role.ENGINEERING_SENIOR_MANAGEMENT_TEAM)
        groups.addFromRole(attending, Role.PEOPLE_EARTHLINGS_OTHERS_NICOLA)

        return groups
    }

    private fun List<MutableList<People>>.addFromRole(attending: Group, role: Role) {
        val peopleToAdd = attending.filter { person -> person.role == role }
        Collections.shuffle(peopleToAdd)

        peopleToAdd.forEach {
            this.minBy { it.size }?.add(it) 
                    ?: throw IllegalStateException("Needed a group to add to but did not find one.")
        }

    }
}