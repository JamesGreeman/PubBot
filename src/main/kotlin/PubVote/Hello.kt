package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.voters.StandardVoteInput
import com.geospock.pubvote.voters.WeightedRandomVoter

typealias Group = List<People>

fun main(args: Array<String>) {

    val votes: List<Vote> = listOf(
            People.STEVE voted {
                Place.WRESTLERS += 10
            },
            People.KAI voted {
                Place.WRESTLERS += 5
                Place.POLONIA += 5
            },
            People.JAMES_G voted {
                Place.WRESTLERS += 5
                Place.HAYMAKERS += 5
            },
            People.ANDRE voted {
                Place.WRESTLERS += 5
                Place.HAYMAKERS +=5
            },
            People.BOB voted {
                Place.HAYMAKERS += 10
            },
            People.XAVI voted {
                Place.WRESTLERS += 5
                Place.POLONIA
            },
            People.SAM voted {
                Place.WRESTLERS += 10
            },
            People.CHRISTOPH voted {
                Place.FORT_SAINT_GEORGE += 10
            },
            People.KATIE voted {
                Place.WRESTLERS += 10
            },
            People.DOM voted {
                Place.WRESTLERS += 10
            },
            People.ALISTAIR voted {
                Place.WRESTLERS += 2
                Place.THE_OLD_SPRING += 2
                Place.FORT_SAINT_GEORGE += 2
                Place.GREEN_DRAGON += 2
                Place.POLONIA += 2
            },
            People.JON voted {},
            People.HUW voted {}
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
        println("$group are going to ${voter.choice}")
        println()
        println()
    })
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