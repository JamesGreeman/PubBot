package com.geospock.pubvote.voters

/**
 * @author James
 */
class StandardVoteInput(val attending: List<String>, val votes: Map<String, Map<String, Int>>) : VoteInput{

}