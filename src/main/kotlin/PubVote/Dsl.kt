package PubVote

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place

class InvalidInputException(message: String) : RuntimeException(message)

class Vote(val person: People) {
    val vote = hashMapOf<Place, Int>()

    operator fun Place.plusAssign(count: Int) {
        vote.merge(this, count, Int::plus)
        if (vote.values.sum() > 10) {
            throw InvalidInputException("$person has cast more than 10 total votes")
        }
    }
}

fun People.voted(init: Vote.() -> Unit): Vote {
    val vote = Vote(this)
    vote.init()
    return vote
}