package com.Java_instagram_clone.domain.feed.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.feed.entity.RequestFeed;
import com.Java_instagram_clone.domain.feed.entity.ResponseFeed;
import com.Java_instagram_clone.domain.feed.kafka.FeedProducer;
import com.Java_instagram_clone.domain.feed.repository.FeedRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.Java_instagram_clone.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final Response responseDto;
    private final FeedRepository feedRepository;
    private final AuthRepository authRepository;
    private final FeedProducer feedProducer;
    private final FileService fileService;

    public ResponseEntity<?> create(MultipartFile[] files, RequestFeed feed) throws Exception {

        ArrayList<String> createFiles = new ArrayList<>();
        try {

            if (files != null) {
                createFiles = fileService.uploadFile(files);
            }

            if (!createFiles.isEmpty()) {

                String file = String.join(",", createFiles);
                Member member = authRepository.findById(feed.getUserNo());

                Feed saveFeed = Feed.builder()
                        .user(member)
                        .contents(feed.getContents())
                        .files(file)
                        .build();

                feedRepository.save(saveFeed);

            }

            feedProducer.sendNotification("새로운 피드가 등록되었습니다." + feed.getContents());


        } catch (IOException e) {
            throw e;
        }

        return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> findAll(RequestFeed feed) {
        return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getFeedByFeedId(long id) {

        Feed feed = feedRepository.findById(id).orElse(null);

        Member member = feed.getUser();

        ResponseFeed responseFeed = new ResponseFeed();
        String[] fileDir = Arrays.stream(feed.getFiles().split(","))
                .map(s -> "uploadFile/" + s)
                .toArray(String[]::new); // 다시 배열로 변환
        responseFeed.setFiles(fileDir);
        responseFeed.setId(feed.getId());
        responseFeed.setContents(feed.getContents());
        responseFeed.setLikes(feed.getLikes());
        responseFeed.setComments(feed.getComments());


        ResponseMember responseMember = new ResponseMember();
        responseMember.setId(member.getId());
        responseMember.setAccountName(member.getAccountName());
        responseFeed.setUser(responseMember);


        return responseDto.success(responseFeed, "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> findByUserId(RequestFeed requestFeed) {

        ArrayList<Feed> feeds = feedRepository.findByUserId(requestFeed.getUserNo());

        List<ResponseFeed> responseFeeds = feeds.stream().map(feed -> {
            Member member = feed.getUser();
            String[] fileDir = Arrays.stream(feed.getFiles().split(","))
                    .map(s -> "uploadFile/" + s)
                    .toArray(String[]::new); // 다시 배열로 변환
            ResponseFeed responseFeed = new ResponseFeed();
            responseFeed.setFiles(fileDir);
            responseFeed.setId(feed.getId());
            responseFeed.setContents(feed.getContents());
            responseFeed.setLikes(feed.getLikes());
            responseFeed.setComments(feed.getComments());

            ResponseMember responseMember = new ResponseMember();
            responseMember.setId(member.getId());
            responseMember.setAccountName(member.getAccountName());
            responseFeed.setUser(responseMember);

            return responseFeed;
        }).toList();

        return responseDto.success(responseFeeds, "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> findByUserIds(RequestFeed feed) {
        return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> update(RequestFeed feed) {
        return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> remove(long id) {

        Feed feed = feedRepository.findById(id).orElse(new Feed());
        String[] files = feed.getFiles().split(",");

        try {
            fileService.removeFile(files);
            feedRepository.deleteById(id);
        } catch (IOException e) {
            return responseDto.fail("파일 삭제중 오류가 발생 했습니다." + e, HttpStatus.BAD_REQUEST);
        }


        return responseDto.success("장상적으로 처리 했습니다.");
    }
}
