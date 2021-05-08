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
 * created @ 6.05.2021
 */

@Table
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LetterboxDUser implements Serializable {
    private static final long serialVersionUID = -8567732518750444103L;

    public static final LetterboxDUser EMPTY = LetterboxDUser.builder().build();

    @PrimaryKey  String username;
    String twitterUsername;
}
