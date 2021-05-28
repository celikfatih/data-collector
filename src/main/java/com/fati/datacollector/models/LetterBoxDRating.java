package com.fati.datacollector.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

/**
 * author @ fati
 * created @ 27.05.2021
 */

@Table
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LetterBoxDRating implements Serializable {
    private static final long serialVersionUID = 2008521968287524388L;

    @PrimaryKey String id;
    String username;
    String film;
    Float rating;
}
