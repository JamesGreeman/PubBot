package com.geospock.pubvote.voters

/**
 * @author James
 */
interface Voter<in VoteInput> {

    fun runVote(input : VoteInput) : String
}