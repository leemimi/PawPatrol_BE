package com.patrol.domain.comment.service;

import com.patrol.api.comment.dto.CommentRequestDto;
import com.patrol.api.comment.dto.CommentResponseDto;
import com.patrol.domain.comment.entity.Comment;
import com.patrol.domain.comment.repository.CommentRepository;
import com.patrol.domain.findPost.entity.FindPost;
import com.patrol.domain.findPost.repository.FindPostRepository;
import com.patrol.domain.lostpost.entity.LostPost;
import com.patrol.domain.lostpost.repository.LostPostRepository;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final FindPostRepository findPostRepository;
    private final LostPostRepository lostPostRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, Member author) {
        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());

        // ✅ 로그인한 사용자 정보 설정
        comment.setAuthor(author);

        // LostPost 조회 후 설정
        if (requestDto.getLostPostId() != null) {
            LostPost lostPost = lostPostRepository.findById(requestDto.getLostPostId())
                    .orElseThrow(() -> new RuntimeException("해당 ID의 실종 신고 게시글을 찾을 수 없습니다."));
            comment.setLostPost(lostPost);
        }

        // FindPost 조회 후 설정
        if (requestDto.getFindPostId() != null) {
            FindPost findPost = findPostRepository.findById(requestDto.getFindPostId())
                    .orElseThrow(() -> new RuntimeException("해당 ID의 제보 게시글을 찾을 수 없습니다."));
            comment.setFindPost(findPost);
        }

        // 저장 후 강제 플러시
        commentRepository.saveAndFlush(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto,  Member author) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 로그인한 사용자(author)가 댓글 작성자와 일치하는지 확인
        if (!comment.getAuthor().equals(author)) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }
        comment.setContent(requestDto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Member author) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        // 로그인한 사용자(author)가 댓글 작성자와 일치하는지 확인
        if (!comment.getAuthor().equals(author)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByFindPost(Long findPostId) {
        List<Comment> comments = commentRepository.findByFindPost_Id(findPostId);  // ✅ 올바른 메서드 호출
        if (comments.isEmpty()) {
            throw new RuntimeException("해당 제보글에 대한 댓글이 없습니다.");
        }
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByLostPost(Long lostPostId) {
        List<Comment> comments = commentRepository.findByLostPost_Id(lostPostId);
        if (comments.isEmpty()) {
            throw new RuntimeException("해당 신고글에 대한 댓글이 없습니다.");
            //return new ArrayList<>();  // Return empty list when no comments

        }
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }


}
