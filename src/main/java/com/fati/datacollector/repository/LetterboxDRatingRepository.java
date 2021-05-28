package com.fati.datacollector.repository;

import com.fati.datacollector.models.LetterBoxDRating;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * author @ fati
 * created @ 27.05.2021
 */

public interface LetterboxDRatingRepository extends CassandraRepository<LetterBoxDRating, String> {
}
