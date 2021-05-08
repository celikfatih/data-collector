package com.fati.datacollector.repository;

import com.fati.datacollector.models.TwitterUser;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * author @ fati
 * created @ 8.05.2021
 */

public interface TwitterUserRepository extends CassandraRepository<TwitterUser, String> {
}
