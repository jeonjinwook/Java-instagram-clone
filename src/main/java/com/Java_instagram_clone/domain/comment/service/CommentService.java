package com.Java_instagram_clone.domain.comment.service;

import com.Java_instagram_clone.domain.auth.entity.Response;
import com.Java_instagram_clone.domain.comment.entity.Comment;
import com.Java_instagram_clone.domain.comment.entity.RequestComment;
import com.Java_instagram_clone.domain.comment.repository.CommentRepository;
import com.Java_instagram_clone.domain.feed.entity.Feed;
import com.Java_instagram_clone.domain.feed.repository.FeedRepository;
import com.Java_instagram_clone.domain.member.entity.Member;
import com.Java_instagram_clone.kafka.FeedProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final Response responseDto;
  private final FeedProducer feedProducer;
  private final FeedRepository feedRepository;
  private final CommentRepository commentRepository;

  @Transactional
  public ResponseEntity<?> create(RequestComment requestComment) {

    Feed feed = feedRepository.findById(requestComment.getFeedId()).orElse(null);

    if (feed != null) {

      Member user = feed.getUser();

      Comment comment =
          Comment.builder().user(user).feed(feed).contents(requestComment.getContents()).build();

      comment = commentRepository.save(comment);

      feedProducer.sendNotification(
          user.getAccountName() + "님이 올리신 " + feed.getContents() + " 게시물에 댓글이 등록되었습니다.",
          String.valueOf(user.getId()));

      return responseDto.success(comment, "정상적으로 댓글이 등록되었습니다.", HttpStatus.OK);
    }

    return responseDto.fail("해당 피드를 찾기 못했습니다. ", HttpStatus.BAD_REQUEST);
  }

  public ResponseEntity<?> delete(RequestComment requestComment) {

    feedRepository.deleteById(requestComment.getFeedId());

    return responseDto.success("정상적으로 댓글이 삭제 되었습니다.");
  }
}
