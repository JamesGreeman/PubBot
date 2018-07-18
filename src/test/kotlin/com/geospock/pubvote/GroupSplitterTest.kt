package com.geospock.pubvote

import com.geospock.pubvote.people.People
import org.junit.Assert.*

import org.junit.Test
import java.lang.Math.abs

class GroupSplitterTest {

    @Test
    fun splitGroups() {
        for (maxGroupSize in 4..10) {
            val splitter = GroupSplitter(maxGroupSize)
            for (numPeople in 4 .. 100) {
                val groupToTest = MutableList(numPeople) { People.JAMES_G }
                val splitGroups = splitter.splitGroups(groupToTest)

                val largestGroupSize = splitGroups.maxBy { it.size }!!.size
                val smallestGroupSize = splitGroups.minBy { it.size }!!.size

                if (abs(largestGroupSize - smallestGroupSize) > 1){
                    fail("Group sizes too different $splitGroups")
                }
            }
        }

    }
}