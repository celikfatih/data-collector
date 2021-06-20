# data-collector

## Introduction
data-collector application is a data collection application. It collects data for use in studies for 
movie recommendation systems.

### What does it use?
It uses the movie review site [Letterboxd](https://letterboxd.com) to collect data. It collects profiles 
that share their `Twitter` profiles on the Letterboxd site and their `1800` movie ratings.

It then collects [Twitter](https://twitter.com) user profiles and the last `3000` tweets using the Twitter usernames 
of those users.

## Used technologies
This work is a [Spring-Boot](https://spring.io/projects/spring-boot) project. This framework provides various 
facilities for database I/O operations and scheduled operations.

[Cassandra](https://cassandra.apache.org) was used as the database. It is preferred because it is `NoSQL`, 
fast when working on big data, and it is an open source project.

[Jsoup](https://jsoup.org) is the most common tool used for web scraping in `Java`. It was used in 
data scraping on the `Letterboxd` site.

[Twitter4J](https://twitter4j.org/en/index.html) was used to collect profiles and related tweets of Twitter users.

Finally, [Lombok](https://projectlombok.org). It saves us from writing various methods such as `Getter/Setter` 
and `Constructor`.

## How to use?
Soon.