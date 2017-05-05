package PubVote

import com.geospock.pubvote.GroupSplitter
import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.voters.StandardVoteInput
import com.geospock.pubvote.voters.WeightedRandomVoter

fun main(args: Array<String>) {

    val votes = mapOf(
            People.STEVE to mapOf(
                    Place.WRESTLERS to 5,
                    Place.FORT_SAINT_GEORGE to 5
            ),
            People.DAVID_W to mapOf(
                    Place.HAYMAKERS to 3,
                    Place.WRESTLERS to 3,
                    Place.FORT_SAINT_GEORGE to 4
            ),
            People.JAMES_G to mapOf(
                    Place.WRESTLERS to 5,
                    Place.FORT_SAINT_GEORGE to 5
            ),
            People.ANDRE to mapOf(
                    Place.FORT_SAINT_GEORGE to 5,
                    Place.GREEN_DRAGON to 5
            ),
            People.BENITA to mapOf(
                    Place.WRESTLERS to 5,
                    Place.THE_OLD_SPRING to 5
            ),
            People.CHRISTOPH to mapOf(
                    Place.THE_OLD_SPRING to 5,
                    Place.FORT_SAINT_GEORGE to 5
            ),
            People.KATIE to mapOf(
                    Place.WRESTLERS to 3,
                    Place.THE_OLD_SPRING to 4,
                    Place.POLONIA to 3
            )
    )

    val groups = GroupSplitter(12).splitGroups(listOf(
            People.KAI,
            People.CHRISTOPH,
            People.JAMES_G,
            People.XAVI,
            People.JON,
            People.BOB,
            People.ARTEM,
            People.DAVID_W,
            People.HUW,
            People.KATIE,
            People.STEVE,
            People.DAVID_B,
            People.BENITA,
            People.ANDRE,
            People.FELIX
    ))

    val groupVotes = consolidateGroupVotes(groups, votes)

    runVote(groupVotes)

}

private fun runVote(groupVotes : Map<List<People>, Map<Place, Int>>){
    val voters = groupVotes.mapValues { WeightedRandomVoter() }
    val groupWinners = mutableMapOf<List<People>, Place>()

    var complete = false
    while (!complete) {
        complete = true
        val winners = mutableSetOf<Place>()
        for ((people, voter) in voters) {
            val winner = voter.runVote(StandardVoteInput(groupVotes.getOrDefault(people, mapOf<Place, Int>())))
            if (winners.contains(winner)){
                complete = false
            }
            groupWinners[people] = winner
            winners.add(winner)
        }
    }

    groupWinners.forEach({ (people, place) ->
        println(voters[people]?.getOutput())
        println("$people are going to $place")
    })
}

private fun consolidateGroupVotes(groups: List<List<People>>, votes: Map<People, Map<Place, Int>>): MutableMap<List<People>, Map<Place, Int>> {
    val groupVotes = mutableMapOf<List<People>, Map<Place, Int>>()
    for (group in groups) {
        val totals = mutableMapOf<Place, Int>()
        group.forEach {
            votes[it]?.forEach { (place, value) ->
                val current = totals.getOrDefault(place, 0)
                totals.put(place, current + value)
            }
        }
        groupVotes.put(group, totals)
    }
    return groupVotes
}