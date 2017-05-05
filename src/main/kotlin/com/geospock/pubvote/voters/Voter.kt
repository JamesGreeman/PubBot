package com.geospock.pubvote.voters

import com.geospock.pubvote.places.Place

/**
 * @author James
 */
interface Voter<in VoteInput> {

    fun runVote(input : VoteInput) : Place

    fun getOutput() : String
}