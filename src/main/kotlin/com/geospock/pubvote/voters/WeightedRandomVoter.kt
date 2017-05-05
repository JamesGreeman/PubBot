package com.geospock.pubvote.voters

import com.geospock.pubvote.places.Place
import java.util.Random

class WeightedRandomVoter() : Voter<StandardVoteInput> {

    private val output = StringBuilder();

    override fun runVote(input: StandardVoteInput): Place {
        output.setLength(0)
        var choice: Place = Place.NONE
        val totalVotes = input.votes.values.sum()

        if (totalVotes == 0) {
            output.appendln("No votes returned - cannot run vote, returning ${Place.NONE}")
            return choice
        }

        val value = Random().nextInt(totalVotes)

        val line0 = StringBuilder("0")
        val line1 = StringBuilder("|")
        val line2 = StringBuilder("|")
        val line3 = StringBuilder("|")
        val line4 = StringBuilder()
        val key = StringBuilder()
        var currentTotal = 0
        var id = 'A'
        var marked = false
        input.votes.forEach({ (place, vote) ->
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

        output.appendln("Random number is: $value")
        output.appendln(line0)
        output.appendln(line1)
        output.appendln(line2)
        output.appendln(line3)
        output.appendln(line4)
        output.appendln(key)

        return choice
    }

    override fun getOutput() : String {
        return output.toString()
    }

    private fun padToLength(builder: StringBuilder, length: Int, padding: String) {
        while (builder.length < length) {
            builder.append(padding)
        }
    }


}