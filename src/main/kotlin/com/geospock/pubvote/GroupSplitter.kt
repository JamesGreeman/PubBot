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

        groups.addFromRole(attending, Role.ELT)
        groups.addFromRole(attending, Role.CSMT)
        groups.addFromRole(attending, Role.ESMT)
        groups.addFromRole(attending, Role.PEON)

        return groups
    }

    private fun List<MutableList<People>>.addFromRole(attending: Group, role: Role) {
        val peopleToAdd = attending.filter { person -> person.role == role }
        Collections.shuffle(peopleToAdd)

        peopleToAdd.forEach {
            this.minBy { it.size }!!
                    .add(it)
        }

    }
}