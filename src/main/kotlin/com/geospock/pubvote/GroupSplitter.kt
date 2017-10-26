package com.geospock.pubvote

import PubVote.Group
import java.util.Collections

/**
 * @author James
 */
class GroupSplitter(private val maxGroupSize: Int) {

    fun splitGroups(attending: Group): List<Group> {
        val groups = (attending.size + maxGroupSize - 1) / maxGroupSize
        val groupSize = (attending.size + groups - 1) / groups

        val listToSplit = attending.toMutableList()
        Collections.shuffle(listToSplit)
        return listToSplit.withIndex()
                .groupBy { it.index / groupSize }
                .map { it.value.map { it.value } }
    }
}