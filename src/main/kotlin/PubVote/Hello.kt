package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.voters.StandardVoteInput
import com.geospock.pubvote.voters.WeightedRandomVoter

typealias Group = List<People>

fun main(args: Array<String>) {

    val votes: List<Vote> = listOf(
            People.KAI voted {},
            People.JON voted {},
            People.DAVID_W voted {
                Place.WRESTLERS += 10
            },
            People.JAMES_G voted {
                Place.WRESTLERS += 10
            },
            People.BOB voted {
                Place.BURLEIGH_ARMS += 10
            },
            People.XAVI voted {
                Place.POLONIA += 6
                Place.BURLEIGH_ARMS += 4
            },
            People.SAM voted {},
            People.FELIX voted {},
            People.HUW voted {
                Place.WRESTLERS += 5
                Place.THE_OLD_SPRING += 5
            },
            People.CHRISTOPH voted {
                Place.POLONIA += 10
            },
            People.CHARLES voted {},
            People.SAM_2 voted {
                Place.THE_OLD_SPRING += 2
                Place.POLONIA += 1
                Place.BURLEIGH_ARMS += 6
            },
            People.FELIX_SG voted {
                Place.WRESTLERS += 3
                Place.POLONIA += 3
                Place.BURLEIGH_ARMS += 4
            },
            People.TRUC voted {
                Place.WRESTLERS += 10
            },
            People.TOM voted {
                Place.THE_OLD_SPRING += 2
                Place.BURLEIGH_ARMS += 8
            },
            People.ALAN voted {
                Place.HOPBINE += 5
                Place.BURLEIGH_ARMS += 5
            },
            People.LUCAS voted {},
            People.NICOLA voted {
                Place.HAYMAKERS += 10
            },
            People.AMARIA voted {},
            People.FIONA voted {},
            People.KATIE voted {
                Place.WRESTLERS += 10
            }
    )

    val groups = GroupSplitter(8).splitGroups(votes.map { it.person })

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
    println("IMPORTANT: Within your groups please decide who will be paying (and expensing the bill) and book a " +
            "table if required.")
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