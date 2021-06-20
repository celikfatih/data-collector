package com.fati.datacollector.repository;

import com.fati.datacollector.model.TwitterTweet;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * author @ fati
 * created @ 12.06.2021
 */

public interface TwitterTweetRepository extends CassandraRepository<TwitterTweet, String> {
}
