package com.fati.datacollector.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author @ fati
 * created @ 10.06.2021
 */

@Table
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TwitterTweet implements Serializable {
    private static final long serialVersionUID = 7541746956626879099L;

    @PrimaryKey
    String id;

    Date createdAt;
    String text;
    String source;
    Boolean truncated;
    String userId;
    Integer retweetCount;
    Integer favoriteCount;
    Set<String> hashtags;
    Integer userMentionsCount;
    Boolean retweet;
    Boolean retweeted;
    Boolean possiblySensitive;
    String lang;
    Boolean favorited;

    public TwitterTweet(Status status) {
        this.id = String.valueOf(status.getId());
        this.createdAt = status.getCreatedAt();
        this.text = status.getText();
        this.source = status.getSource();
        this.truncated = status.isTruncated();
        this.userId = String.valueOf(status.getUser().getId());
        this.retweetCount = status.getRetweetCount();
        this.favoriteCount = status.getFavoriteCount();
        this.hashtags = Arrays.stream(status.getHashtagEntities())
                .map(HashtagEntity::getText)
                .collect(Collectors.toSet());
        this.userMentionsCount = Math.toIntExact(Arrays.stream(status.getUserMentionEntities()).count());
        this.retweet = status.isRetweet();
        this.retweeted = status.isRetweeted();
        this.possiblySensitive = status.isPossiblySensitive();
        this.lang = status.getLang();
        this.favorited = status.isFavorited();
    }
}
