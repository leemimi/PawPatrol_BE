package com.patrol.api.comment.controller;

import com.patrol.api.comment.dto.CommentRequestDto;
import com.patrol.api.comment.dto.CommentResponseDto;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.comment.service.CommentService;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 등록")
    public RsData<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto,  @LoginUser Member loginUser) {
        CommentResponseDto responseDto = commentService.createComment(requestDto,loginUser);
        return new RsData<>("200", "댓글이 등록되었습니다.", responseDto);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정")
    public RsData<CommentResponseDto> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentRequestDto requestDto, @LoginUser Member loginUser) {
        CommentResponseDto responseDto = commentService.updateComment(commentId, requestDto,loginUser);
        return new RsData<>("200", "댓글이 수정되었습니다.", responseDto);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public RsData<Void> deleteComment(@PathVariable(name = "commentId")  Long commentId ,@LoginUser Member loginUser) {
        commentService.deleteComment(commentId,loginUser);
        return new RsData<>("200", "댓글이 삭제되었습니다.");
    }

    @GetMapping("/lost-foundposts/{postId}")
    @Operation(summary = "신고글에 대한 댓글 조회")
    public RsData<List<CommentResponseDto>> getCommentsByLostPost(@PathVariable(name = "postId") Long lostPostId) {
        List<CommentResponseDto> comments = commentService.getCommentsByLostFoundPost(lostPostId);
        return new RsData<>("200", "댓글 목록 조회 성공", comments);
    }

}

