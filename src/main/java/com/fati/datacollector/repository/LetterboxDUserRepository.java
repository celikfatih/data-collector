package com.fati.datacollector.repository;

import com.fati.datacollector.models.LetterboxDUser;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * author @ fati
 * created @ 6.05.2021
 */

public interface LetterboxDUserRepository extends CassandraRepository<LetterboxDUser, String> {
}
