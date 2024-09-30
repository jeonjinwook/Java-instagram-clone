package com.Java_instagram_clone.domain.feed.service;

import com.Java_instagram_clone.cmn.EntityToDtoService;
import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.auth.repository.AuthRepository;
import com.Java_instagram_clone.domain.comment.entity.ResponseComment;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.feed.entity.RequestFeed;
import com.Java_instagram_clone.domain.feed.entity.ResponseFeed;
import com.Java_instagram_clone.domain.feed.repository.FeedRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.Java_instagram_clone.file.FileService;
import com.Java_instagram_clone.kafka.FeedProducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final Response responseDto;
  private final FeedRepository feedRepository;
  private final AuthRepository authRepository;
  private final FeedProducer feedProducer;
  private final FileService fileService;
  private final EntityToDtoService entityToDtoService;


  public List<ResponseComment> commentsToResponseComments(Feed feed) {

    return feed.getComments().stream().map(comment -> {

      ResponseComment responseComment = entityToDtoService.convertToDto(comment,
          ResponseComment.class);
      ResponseMember responseMember = entityToDtoService.convertToDto(comment.getUser(),
          ResponseMember.class);

      responseComment.setUser(responseMember);

      return responseComment;
    }).toList();
  }

  public ResponseEntity<?> create(MultipartFile[] files, RequestFeed feed) throws Exception {

    List<String> createFiles = new ArrayList<>();
    try {

      if (files != null) {
        createFiles = fileService.uploadFiles(files);
      }

      if (!createFiles.isEmpty()) {

        Member member = authRepository.findById(feed.getUserNo());

        Feed saveFeed = Feed.builder()
            .user(member)
            .contents(feed.getContents())
            .files(createFiles.toArray(new String[0]))
            .build();

        feedRepository.save(saveFeed);

      }

      feedProducer.sendNotification("새로운 피드가 등록되었습니다. " + feed.getContents(),
          String.valueOf(feed.getUserNo()));


    } catch (IOException e) {
      throw e;
    }

    return responseDto.success("장상적으로 처리 했습니다.");
  }

  public ResponseEntity<?> findAll(RequestFeed feed) {
    return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
  }

  public ResponseEntity<?> getFeedByFeedId(long id) {

    Feed feed = feedRepository.findById(id).orElse(null);

    ResponseFeed responseFeed = entityToDtoService.convertToDto(feed, ResponseFeed.class);
    ResponseMember responseMember = entityToDtoService.convertToDto(
        Objects.requireNonNull(feed).getUser(), ResponseMember.class);

    responseFeed.setUser(responseMember);

    responseFeed.setComments(commentsToResponseComments(feed));

    return responseDto.success(responseFeed, "장상적으로 처리 했습니다.", HttpStatus.OK);
  }

  public ResponseEntity<?> findByUserId(RequestFeed requestFeed) {

    ArrayList<Feed> feeds = feedRepository.findByUserId(requestFeed.getUserNo());

    List<ResponseFeed> responseFeeds = feeds.stream().map(feed -> {

      ResponseFeed responseFeed = entityToDtoService.convertToDto(feed, ResponseFeed.class);
      ResponseMember responseMember = entityToDtoService.convertToDto(
          Objects.requireNonNull(feed).getUser(), ResponseMember.class);

      responseFeed.setUser(responseMember);

      responseFeed.setComments(commentsToResponseComments(feed));

      return responseFeed;
    }).toList();

    return responseDto.success(responseFeeds, "장상적으로 처리 했습니다.", HttpStatus.OK);
  }

  public ResponseEntity<?> update(RequestFeed feed) {
    return responseDto.success("", "장상적으로 처리 했습니다.", HttpStatus.OK);
  }

  public ResponseEntity<?> remove(long id) {

    Feed feed = feedRepository.findById(id).orElse(new Feed());

    fileService.removeFiles(feed.getFiles());
    feedRepository.deleteById(id);

    return responseDto.success("장상적으로 처리 했습니다.");
  }
}
