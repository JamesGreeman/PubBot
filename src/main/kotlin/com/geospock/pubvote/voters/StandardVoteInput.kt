package com.geospock.pubvote.voters

import com.geospock.pubvote.people.People
import com.geospock.pubvote.places.Place

/**
 * @author James
 */
class StandardVoteInput(val attending: List<People>, val votes: Map<People, Map<Place, Int>>) : VoteInput{

}