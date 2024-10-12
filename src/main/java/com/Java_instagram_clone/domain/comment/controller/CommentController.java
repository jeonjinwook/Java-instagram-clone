package com.Java_instagram_clone.domain.comment.controller;

import com.Java_instagram_clone.domain.comment.entity.RequestComment;
import com.Java_instagram_clone.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("create")
  public ResponseEntity<?> create(@RequestBody RequestComment requestComment) {

    return commentService.create(requestComment);
  }

  @PostMapping("delete")
  public ResponseEntity<?> delete(@RequestBody RequestComment requestComment) {

    return commentService.delete(requestComment);
  }
}
