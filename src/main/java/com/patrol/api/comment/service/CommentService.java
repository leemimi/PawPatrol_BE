package com.patrol.api.comment.service;
/*
import com.patrol.api.Comment.dto.CommentRequestDto;
import com.patrol.api.Comment.dto.CommentResponseDto;
import com.patrol.domain.Comment.entity.Comment;
import com.patrol.domain.Comment.repository.CommentRepository;
import com.patrol.domain.LostPost.entity.LostPost;
import com.patrol.domain.lostFoundPost.entity.FindPost;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        Comment comment = new Comment();
        comment.setContent(requestDto.getContent());
        // Member를 데이터베이스에서 조회
        Member author = memberRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다."));
        comment.setAuthor(author);
        if (requestDto.getLostPostId() != null) {
            comment.setLostPost(new LostPost(requestDto.getLostPostId())); // LostPost 엔티티 수정됨
        }
        if (requestDto.getFindPostId() != null) {
            comment.setLostFound(new FindPost(requestDto.getFindPostId())); // FindPost 엔티티 수정됨
        }
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.setContent(requestDto.getContent());
        return new CommentResponseDto(comment);
    }
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByLostPost(Long lostPostId) {
        return commentRepository.findByLostPost_LostId(lostPostId)
                .stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByFindPost(Long findPostId) {
        return commentRepository.findByLostFound_FoundId(findPostId)
                .stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
*/