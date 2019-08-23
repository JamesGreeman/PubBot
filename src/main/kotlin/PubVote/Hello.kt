package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.collectors.CommandLineVoteCollector
import com.geospock.pubvote.people.People
import com.geospock.pubvote.people.People.AARON
import com.geospock.pubvote.people.People.ANIBAL
import com.geospock.pubvote.people.People.ARREN
import com.geospock.pubvote.people.People.BOB
import com.geospock.pubvote.people.People.CHARLES
import com.geospock.pubvote.people.People.FELIX_M
import com.geospock.pubvote.people.People.GARETH
import com.geospock.pubvote.people.People.GEORGE_P
import com.geospock.pubvote.people.People.HUGO
import com.geospock.pubvote.people.People.ISRAEL
import com.geospock.pubvote.people.People.JON
import com.geospock.pubvote.people.People.JUANJO
import com.geospock.pubvote.people.People.KAI
import com.geospock.pubvote.people.People.KATIE
import com.geospock.pubvote.people.People.NICK
import com.geospock.pubvote.people.People.PAULO
import com.geospock.pubvote.people.People.REBECCA
import com.geospock.pubvote.people.People.SAM_C
import com.geospock.pubvote.people.People.TOM_B
import com.geospock.pubvote.people.People.TOM_C
import com.geospock.pubvote.people.People.TRUC
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
                JON voted {
                },
                BOB voted {
                    OTHERSYDE += 10
                },
                FELIX_M voted {
                    OTHERSYDE += 10
                },
                CHARLES voted {
                },
                SAM_C voted {
                    BURLEIGH_ARMS += 3
                    OTHERSYDE += 6
                },
                TOM_B voted {
                    WRESTLERS += 3
                    POLONIA += 1
                    HAYMAKERS += 6
                },
                TRUC voted {
                    WRESTLERS += 5
                    HAYMAKERS += 5
                },
                NICK voted {
                    OTHERSYDE += 10
                },
                TOM_C voted {
                },
                HUGO voted {
                    WRESTLERS += 6
                    OLD_SPRING += 4
                },
                ARREN voted {
                    BURLEIGH_ARMS += 10
                },
                ISRAEL voted {
                    FORT_SAINT_GEORGE += 5
                    OLD_SPRING += 5
                },
                KATIE voted {
                    WRESTLERS += 5
                    HAYMAKERS += 5
                },
                AARON voted {
                },
                PAULO voted {
                    WRESTLERS += 3
                    HAYMAKERS += 3
                    OLD_SPRING += 3
                },
                GEORGE_P voted {
                    WRESTLERS += 3
                    WATERMAN += 3
                    HAYMAKERS += 1
                    OLD_SPRING += 3
                },
                REBECCA voted {
                    WRESTLERS += 5
                    OLD_SPRING += 5
                },
                JUANJO voted {
                    WRESTLERS += 5
                    OLD_SPRING += 5
                },
                JUANJO voted {
                    HAYMAKERS += 4
                    OTHERSYDE += 6
                },
                ANIBAL voted {
                    WRESTLERS += 2
                    HAYMAKERS += 4
                    OLD_SPRING += 4
                },
                GARETH voted {
                    WATERMAN += 2
                    HAYMAKERS += 4
                    OTHERSYDE += 4
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