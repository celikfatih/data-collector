package com.fati.datacollector.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import twitter4j.User;

import java.io.Serializable;
import java.util.Date;

/**
 * author @ fati
 * created @ 10.06.2021
 */

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TwitterUser implements Serializable {
    private static final long serialVersionUID = 5882477328310729699L;

    @PrimaryKey
    String id;

    String name;
    String screenName;
    String location;
    String url;
    String description;
    Boolean verified;
    Integer followersCount;
    Integer friendsCount;
    Integer listedCount;
    Integer favouritesCount;
    Integer statusesCount;
    Date createdAt;
    Boolean geoEnabled;
    String lang;
    Boolean contributorsEnabled;
    Boolean isTranslator;
    Boolean defaultProfile;
    Boolean defaultProfileImage;
    String profileBackgroundColor;
    Boolean profileBackgroundTiled;
    Boolean profileUseBackgroundImage;
    Boolean isVerified;

    public TwitterUser(User user) {
        this.id = String.valueOf(user.getId());
        this.name = user.getName();
        this.screenName = user.getScreenName();
        this.location = user.getLocation();
        this.url = user.getURL();
        this.description = user.getDescription();
        this.verified = user.isVerified();
        this.followersCount = user.getFollowersCount();
        this.friendsCount = user.getFriendsCount();
        this.listedCount = user.getListedCount();
        this.favouritesCount = user.getFavouritesCount();
        this.statusesCount = user.getStatusesCount();
        this.createdAt = user.getCreatedAt();
        this.geoEnabled = user.isGeoEnabled();
        this.lang = user.getLang();
        this.contributorsEnabled = user.isContributorsEnabled();
        this.isTranslator = user.isTranslator();
        this.defaultProfile = user.isDefaultProfile();
        this.defaultProfileImage = user.isDefaultProfileImage();
        this.profileBackgroundColor = user.getProfileBackgroundColor();
        this.profileBackgroundTiled = user.isProfileBackgroundTiled();
        this.profileUseBackgroundImage = user.isProfileUseBackgroundImage();
        this.isVerified = user.isVerified();
    }
}
