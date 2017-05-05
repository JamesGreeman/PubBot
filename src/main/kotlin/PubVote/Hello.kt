package PubVote

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import com.geospock.pubvote.voters.SplittingVoter
import com.geospock.pubvote.voters.StandardVoteInput

fun main(args: Array<String>) {
    print(SplittingVoter(8).runVote(StandardVoteInput(
            listOf(People.KAI, People.CHRISTOPH, People.JAMES_G, People.XAVI, People.JON, People.BOB, People.ARTEM,
                    People.DAVID_W, People.HUW, People.KATIE, People.STEVE, People.DAVID_B, People.BENITA, People.ANDRE,
                    People.FELIX),
            mapOf(
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
            ))))
}

