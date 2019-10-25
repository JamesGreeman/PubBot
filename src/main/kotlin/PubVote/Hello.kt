package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.collectors.CommandLineVoteCollector
import com.geospock.pubvote.people.People
import com.geospock.pubvote.people.People.*
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.places.Place.BURLEIGH_ARMS
import com.geospock.pubvote.places.Place.FORT_SAINT_GEORGE
import com.geospock.pubvote.places.Place.HAYMAKERS
import com.geospock.pubvote.places.Place.OLD_SPRING
import com.geospock.pubvote.places.Place.OTHERSYDE
import com.geospock.pubvote.places.Place.POLONIA
import com.geospock.pubvote.places.Place.WATERMAN
import com.geospock.pubvote.places.Place.WRESTLERS
import com.geospock.pubvote.voters.StandardVoteInput
import com.geospock.pubvote.voters.WeightedRandomVoter

typealias Group = List<People>

fun main(args: Array<String>) {
    val useCommandline = false
    val maxGroupSize = 8

    val votes: List<Vote> = if (useCommandline) {
        CommandLineVoteCollector().collectVotes()
    } else {
        listOf(
                KAI voted {
                },
                BOB voted {
                    HAYMAKERS += 10
                },
                XAVI voted {
                },
                ALISTAIR voted {
                    WATERMAN += 10
                },
                SAM_C voted {
                    OLD_SPRING += 4
                    BURLEIGH_ARMS += 5
                },
                TOM_B voted {
                    POLONIA += 7
                    HAYMAKERS += 3
                },
                TRUC voted {
                },
                FLAVIA voted {
                    WRESTLERS += 5
                    HAYMAKERS += 5
                },
                HUGO voted {
                    WRESTLERS += 7
                    HAYMAKERS += 3
                },
                STEPHANIE voted {
                    WRESTLERS += 10
                },
                ARREN voted {
                    HAYMAKERS += 10
                },
                KATIE voted {
                    WRESTLERS += 5
                    HAYMAKERS += 5
                },
                KAREN voted {
                    HAYMAKERS += 5
                    OLD_SPRING += 5
                },
                PAULO voted {
                    WRESTLERS += 5
                    OLD_SPRING += 5
                },
                PRASANTH voted {
                },
                GEORGE_P voted {
                    WRESTLERS += 5
                    OLD_SPRING += 5
                },
                JUANJO voted {
                    HAYMAKERS += 4
                    OLD_SPRING += 6
                },
                GARETH voted {
                    HAYMAKERS += 8
                    OLD_SPRING += 2
                }
        )
    }

    val groups = GroupSplitter(maxGroupSize).splitGroups(votes.map { it.person })

    val voteMap = votes.associate { (person, vote) -> person to vote }
    val groupVotes = consolidateGroupVotes(groups, voteMap)

    runVote(groupVotes)

}

private fun runVote(groupVotes: Map<Group, Map<Place, Int>>) {
    val voters = groupVotes.mapValues { WeightedRandomVoter() }

    var complete = false
    while (!complete) {
        complete = true
        val winners = mutableSetOf<Place>()
        for ((people, voter) in voters) {
            val winner = voter.runVote(StandardVoteInput(groupVotes.getOrDefault(people, emptyMap())))
            if (winners.contains(winner)) {
                complete = false
            }
            winners.add(winner)
        }
    }

    voters.forEach({ (group, voter) ->
        println(voter.getOutput())
        println("${group.prettify()} are going to *${voter.choice.prettyName}* ${voter.choice.slackString} - @${group.first().slackHandle} is Group Leader.")
        println()
        println()
    })
    println("IMPORTANT: Group leaders - please ensure a table is booked if necessary and decide who will be paying.")
}


private fun Group.prettify(): String {
    val handleList = this.joinToString(", ") { "@${it.slackHandle}" }
    return "$handleList (Group size is *${this.size}*)"
}

private fun consolidateGroupVotes(groups: List<Group>, votes: Map<People, Map<Place, Int>>): Map<Group, Map<Place, Int>> {
    return groups.map { group ->
        val totals = mutableMapOf<Place, Int>()
        group.forEach {
            votes[it]!!.forEach { (place, value) ->
                totals.merge(place, value, Int::plus)
            }
        }
        group to totals
    }.toMap()
}