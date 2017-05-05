package com.geospock.pubvote

import com.geospock.pubvote.people.People
import java.util.Collections

/**
 * @author James
 */
class GroupSplitter(val maxGroupSize : Int) {

    val paddingValue = maxGroupSize - 1;

    fun splitGroups(attending: List<People>): List<List<People>> {

        val groups = (attending.size + paddingValue) / maxGroupSize
        val groupSize = (attending.size + groups - 1) / groups

        val listToSplit = attending.toMutableList()
        Collections.shuffle(listToSplit)
        val split = listToSplit.withIndex()
                .groupBy { it.index / groupSize }
                .map { it.value.map { it.value } }

        return split
    }
}