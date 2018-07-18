package com.geospock.pubvote.collectors

import PubVote.Vote

interface VoteCollector {

    fun collectVotes(): List<Vote>

}