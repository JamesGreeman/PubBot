package PubVote

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place
import java.lang.IllegalArgumentException

class InvalidInputException(message: String) : RuntimeException(message)

class Vote(val person: People) {
    val vote = hashMapOf<Place, Int>()

    operator fun Place.plusAssign(count: Int) {
        if (count <= 0) {
            throw InvalidInputException("Votes must be greater than 0")
        }
        vote.merge(this, count, Int::plus)
        if (vote.values.sum() > 10) {
            throw InvalidInputException("$person has cast more than 10 total votes")
        }
    }

    fun addVote(place: Place, count: Int) {
        vote[place] = count
    }

    override fun toString(): String {
        return "Vote(person=$person, vote=$vote)"
    }

}

infix fun People.voted(init: Vote.() -> Unit): Vote {
    val vote = Vote(this)
    vote.init()
    return vote
}