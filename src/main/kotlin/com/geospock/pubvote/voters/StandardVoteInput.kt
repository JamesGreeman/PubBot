package com.geospock.pubvote.voters

import com.geospock.pubvote.places.Place

/**
 * @author James
 */
class StandardVoteInput(val votes: Map<Place, Int>) : VoteInput