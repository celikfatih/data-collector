package com.fati.datacollector.runner;

import com.fati.datacollector.service.LetterboxDService;
import com.fati.datacollector.service.TwitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * author @ fati
 * created @ 27.05.2021
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRunner {

    private final LetterboxDService letterboxDService;
    private final TwitterService twitterService;

    @Scheduled(cron = "0 53 00 * * *")
    public void letterboxDJobRunner() {
        log.info("Started letterboxDJobRunner...");
        letterboxDService.extractAndSaveLetterboxDData(10);
        log.info("Ended letterboxDJobRunner...");
    }

    @Scheduled(cron = "0 59 00 * * *")
    public void twitterJobRunner() {
        log.info("Started twitterJobRunner ...");
        twitterService.extractAndSaveTweets();
        log.info("Ended twitterJobRunner...");
    }
}
