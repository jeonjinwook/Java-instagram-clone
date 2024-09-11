package com.Java_instagram_clone.domain.feed.controller;

import com.Java_instagram_clone.domain.feed.entity.RequestFeed;
import com.Java_instagram_clone.domain.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestPart("file") MultipartFile[] files,
                                    @RequestPart("feed") RequestFeed feed) throws Exception {
        return feedService.create(files, feed);
    }

    @GetMapping("findAll")
    public ResponseEntity<?> findAll(RequestFeed feed) {

        return feedService.findAll(feed);
    }

    @GetMapping("getFeedByFeedId")
    public ResponseEntity<?> getFeedByFeedId(@RequestParam("feedId") long id) {
        return feedService.getFeedByFeedId(id);
    }

    @PostMapping("userFeeds")
    public ResponseEntity<?> findByUserId(RequestFeed feed) {

        return feedService.findByUserId(feed);
    }

    @PostMapping("findByUserIds")
    public ResponseEntity<?> findByUserIds(RequestFeed feed) {

        return feedService.findByUserIds(feed);
    }

    @PatchMapping("update")
    public ResponseEntity<?> update(RequestFeed feed) {

        return feedService.update(feed);
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestParam("feedId") long id) {

        return feedService.remove(id);
    }

}
