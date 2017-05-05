package PubVote

import com.geospock.pubvote.voters.SplittingVoter
import com.geospock.pubvote.voters.StandardVoteInput

fun main(args: Array<String>) {
    println("Output: ")
    print(SplittingVoter().runVote(StandardVoteInput(
            listOf("Kai", "Christoph", "James", "Xavi", "Jon", "Bob", "Artem", "David W", "Huw", "Katie", "Steve", "David B", "Benita", "Andre", "Felix"),
            mapOf(
                    "Steve" to mapOf(
                            "Wrestlers" to 5,
                            "Fort Saint George" to 5
                    ),
                    "David W" to mapOf(
                            "Haymakers" to 3,
                            "Wrestlers" to 3,
                            "Fort Saint George" to 4
                    ),
                    "James" to mapOf(
                            "Wrestlers" to 5,
                            "Fort Saint George" to 5
                    ),
                    "Andre" to mapOf(
                            "Fort Saint George" to 5,
                            "Green Dragon" to 5
                    ),
                    "Benita" to mapOf(
                            "Wrestlers" to 5,
                            "The Old Spring" to 5
                    ),
                    "Christoph" to mapOf(
                            "The Old Spring" to 5,
                            "Fort Saint George" to 5
                    ),
                    "Katie" to mapOf(
                            "Wrestlers" to 3,
                            "The Old Spring" to 4,
                            "Polonia" to 3
                    )
            ))))
}

