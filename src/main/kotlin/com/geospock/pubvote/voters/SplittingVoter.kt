package com.geospock.pubvote.voters

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import java.util.Collections
import java.util.Random

class SplittingVoter(val maxGroupSize: Int) : Voter<StandardVoteInput> {
    val paddingValue = maxGroupSize - 1


    override fun runVote(input: StandardVoteInput): String {
        val split = splitList(input.attending)

        val builder = StringBuilder("Groups are: \n")
        val groupTotals = mutableMapOf<String, Map<Place, Int>>()
        var groupName = 1
        split.forEach {
            builder.appendln("Group $groupName: $it")
            groupTotals["" + groupName] = totalVote(it, input.votes);
            groupName++
        }

        var done = false
        val outputs = mutableMapOf<String, Pair<Place, StringBuilder>>()
        while (!done) {
            done = true;
            val choices = mutableSetOf<Place>()
            groupTotals.forEach({ groupName, totals ->
                val groupTotal = totals.values.sum()
                val randomNumber = Random().nextInt(groupTotal);
                val (choice, groupBuilder) = runGroupVote(randomNumber, totals)
                if (choices.contains(choice)) {
                    done = false;
                }
                choices.add(choice)
                outputs[groupName] = choice to groupBuilder
            })
        }

        outputs.forEach({ groupName, (choice, groupBuilder) ->
            builder.appendln("\n\nGroup $groupName summary")
            builder.appendln(groupBuilder)
            builder.appendln("Group $groupName is going to $choice")
        })

        return builder.toString()
    }

    fun totalVote(group: List<People>, votes: Map<People, Map<Place, Int>>): Map<Place, Int> {
        val totals = mutableMapOf<Place, Int>()

        group.forEach { person ->
            val personVotes = votes[person]
            if (personVotes != null) {
                val total = personVotes.values.sum()
                if (total == 10) {
                    personVotes.forEach({ pub, vote ->
                        val current = totals.getOrPut(pub, { 0 })
                        totals.put(pub, current + vote)
                    })
                } else {
                    println("Not counting $person's votes as there should be a total of 10 but found $total")
                }
            }
        }
        return totals
    }

    private fun runGroupVote(value: Int, votes: Map<Place, Int>): Pair<Place, StringBuilder> {
        val line0 = StringBuilder("0")
        val line1 = StringBuilder("|")
        val line2 = StringBuilder("|")
        val line3 = StringBuilder("|")
        val line4 = StringBuilder()
        val key = StringBuilder()
        var choice: Place = Place.NONE
        var currentTotal = 0
        var id = 'A'
        var marked = false
        votes.forEach({ (place, vote) ->
            key.append(id)
            key.append(" - ")
            key.append(place)
            key.append("\n")
            currentTotal += vote

            if (value < currentTotal && !marked) {
                val positionToMark = line3.length + value - currentTotal + vote
                padToLength(line3, positionToMark, " ")
                padToLength(line4, positionToMark, " ")
                line3.append("^")
                line4.append(value)
                marked = true
                choice = place
            }


            val requiredLength = line1.length + vote
            padToLength(line0, requiredLength, " ")
            padToLength(line1, line1.length + vote / 2, " ")
            if (vote > 0) {
                line1.append(id)
            }
            id++
            padToLength(line1, requiredLength, " ")
            padToLength(line2, requiredLength, "-")
            padToLength(line3, requiredLength, " ")

            if (vote > 0) {
                line0.append(currentTotal)
            }
            line1.append("|")
            line2.append("|")
            line3.append("|")
        })

        val builder = StringBuilder("Random number is: $value \n")
        builder.appendln(line0)
        builder.appendln(line1)
        builder.appendln(line2)
        builder.appendln(line3)
        builder.appendln(line4)
        builder.appendln(key)
        return choice to builder
    }


    private fun padToLength(builder: StringBuilder, length: Int, padding: String) {
        while (builder.length < length) {
            builder.append(padding)
        }
    }

    fun splitList(attending: List<People>): List<List<People>> {

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