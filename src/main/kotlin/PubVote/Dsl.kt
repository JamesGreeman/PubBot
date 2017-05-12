package PubVote

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place

class InvalidInputException(message: String) : RuntimeException(message)

class Vote(val person: People) {
    val vote = hashMapOf<Place, Int>()

    operator fun Place.plus(count: Int) {
        vote[this] = count
        if (vote.values.sum() > 10) {
            throw InvalidInputException("$person has cast more than 10 total votes")
        }
    }

    infix fun Int.to(place: Place) {
        vote[place] = this
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