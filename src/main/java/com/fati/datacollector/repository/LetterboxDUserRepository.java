package com.fati.datacollector.repository;

import com.fati.datacollector.models.LetterboxDUser;
import com.fati.datacollector.projection.OnlyTwitterUsername;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

/**
 * author @ fati
 * created @ 6.05.2021
 */

public interface LetterboxDUserRepository extends CassandraRepository<LetterboxDUser, String> {
    boolean existsLetterboxDUserByUsername(String username);
    List<OnlyTwitterUsername> findAllBy();
}
