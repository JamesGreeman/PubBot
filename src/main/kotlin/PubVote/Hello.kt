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
                JAMES_G voted {
                    WRESTLERS += 10
                },
                XAVI voted {
                    HAYMAKERS += 10
                },
                SAM_S voted {
                },
                DAVID_BI voted {
                    HAYMAKERS += 10
                },
                CHARLES voted {
                },
                SAM_C voted {
                    BURLEIGH_ARMS += 6
                    OLD_SPRING += 3
                },
                FELIX_SG voted {
                    WRESTLERS += 10
                },
                TOM_C voted {
                },
                SERGEJ voted {
                },
                HUGO voted {
                    WRESTLERS += 10
                },
                STEPHANIE voted {
                    WRESTLERS += 10
                },
                ARREN voted {
                    WRESTLERS += 5
                    BURLEIGH_ARMS += 5
                },
                SARAH voted {
                    WRESTLERS += 5
                    HAYMAKERS += 5
                },
                KATIE voted {
                    WRESTLERS += 10
                },
                AMY voted {
                    WRESTLERS += 10
                },
                PAULO voted {
                    WRESTLERS += 3
                    HAYMAKERS += 3
                    OLD_SPRING += 4
                },
                PRASANTH voted {

                },
                GEORGE_P voted {
                    WRESTLERS += 10
                },
                JUANJO voted {
                    WRESTLERS += 5
                    OLD_SPRING += 5
                },
                CHRIS voted {
                    WRESTLERS += 10
                },
                GARETH voted {
                    WRESTLERS += 2
                    WATERMAN += 5
                    HAYMAKERS += 3
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