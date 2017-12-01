package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.places.Place.HAYMAKERS
import com.geospock.pubvote.places.Place.POLONIA
import com.geospock.pubvote.places.Place.WRESTLERS
import com.geospock.pubvote.voters.StandardVoteInput
import com.geospock.pubvote.voters.WeightedRandomVoter

typealias Group = List<People>

fun main(args: Array<String>) {

    val votes: List<Vote> = listOf(
            People.KAI voted {
                WRESTLERS += 2
                Place.HOPBINE += 5
                POLONIA += 3
            },
            People.DAVID_W voted {
                Place.HAYMAKERS += 5
                Place.WRESTLERS += 5
            },
            People.JAMES_G voted {
                Place.HOPBINE += 10
            },
            People.ANDRE voted {
                WRESTLERS += 10
            },
            People.BOB voted {
                HAYMAKERS += 10
            },
            People.XAVI voted {
                Place.HAYMAKERS += 2
                Place.HOPBINE += 8
            },
            People.KATIE voted {
                POLONIA += 10
            },
            People.DOM voted {
                Place.WRESTLERS += 10
            },
            People.ARTEM voted {
                Place.POLONIA += 10
            },
            People.ALISTAIR voted {
                Place.HAYMAKERS += 10
            },
            People.SAM_2 voted {
                Place.THE_WATERMAN += 9
            },
            People.JON voted {},
            People.DAVID_BINNS voted {},
            People.SAM voted {},
            People.HUW voted {},
            People.CHARLES voted {}

    )

    val groups = GroupSplitter(10).splitGroups(votes.map { it.person })

    val voteMap = votes
            .map { it.person to it.vote }
            .toMap()
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
            val winner = voter.runVote(StandardVoteInput(groupVotes.getOrDefault(people, mapOf<Place, Int>())))
            if (winners.contains(winner)) {
                complete = false
            }
            winners.add(winner)
        }
    }

    voters.forEach({ (group, voter) ->
        println(voter.getOutput())
        println("${group.prettify()} are going to *${voter.choice.prettyName}* ${voter.choice.slackString}")
        println()
        println()
    })
}


private fun Group.prettify() : String {
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