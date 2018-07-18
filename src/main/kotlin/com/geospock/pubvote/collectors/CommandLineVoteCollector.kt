package com.geospock.pubvote.collectors

import PubVote.Vote
import PubVote.voted
import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place

class CommandLineVoteCollector : VoteCollector {
    override fun collectVotes(): List<Vote> {
        val filtered = People.values()
                .filter { askQuestion("Is ${it.slackHandle} coming to the Pub?") }
                .map {
                    if (askQuestion("Is ${it.slackHandle} placing any votes?")) {
                        it.getVotes()
                    }else {
                        Vote(it)
                    }
                }
        println(filtered)
        return filtered
    }

    private fun askQuestion(message:String): Boolean {
        var response: String?
        do {
            println("$message [y/n]")
            response = readLine()?.toLowerCase()
        } while (response == null || response !in listOf("y", "n"))

        return response == "y"
    }

    private fun Place.getVotes(remaining: Int): Int {
        var response: Int
        do {
            println("Place a vote for ${this.prettyName} [0 - $remaining]")
            response = readLine()?.toIntOrNull() ?: -1
        } while (response > remaining || response < 0)
        return response
    }

    private fun People.getVotes() : Vote {
        val vote =  Vote(this)
        var remainingVotes = 10
        for (place in Place.values()) {
            if (remainingVotes == 0) {
                break
            }
            val votes = place.getVotes(remainingVotes)
            remainingVotes -= votes
            vote.addVote(place, votes)
        }
        return vote
    }

}

fun main(args: Array<String>) {
    CommandLineVoteCollector().collectVotes()
}