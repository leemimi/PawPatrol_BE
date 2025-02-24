package com.patrol.api.lostpost.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.patrol.api.findPost.dto.FindPostRequestDto;
import com.patrol.api.findPost.dto.FindPostResponseDto;
import com.patrol.api.lostpost.dto.LostPostRequestDto;
import com.patrol.api.lostpost.dto.LostPostResponseDto;
import com.patrol.domain.lostpost.service.LostPostService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lost-found/lost")
@RequiredArgsConstructor
public class LostPostController {

    private final LostPostService lostPostService;
    private final ObjectMapper objectMapper;  // JSON 파싱을 위한 ObjectMapper

    @PostMapping
    @Operation(summary = "실종 신고 게시글 등록")
    public RsData<LostPostResponseDto> createLostPost(
            @RequestParam("metadata") String metadataJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());

        try {
            // JSON을 DTO로 변환
            LostPostRequestDto requestDto = objectMapper.readValue(metadataJson, LostPostRequestDto.class);

            // 게시글 생성
            LostPostResponseDto responseDto = lostPostService.createLostPost(requestDto, images, loginUser);
            System.out.println(metadataJson);
            return new RsData<>("200", "실종 신고 게시글을 성공적으로 등록했습니다.", responseDto);

        } catch (Exception e) {
            return new RsData<>("500", "게시글 등록 중 오류가 발생했습니다.", null);
        }
    }


    // 실종 신고 게시글 수정
    @PutMapping("/{postId}")
    @Operation(summary = "실종 신고 게시글 수정")
    public RsData<LostPostResponseDto> updateLostPost(
            @RequestParam("metadata") String metadataJson,
            @PathVariable(name = "postId") Long postId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @LoginUser Member loginUser) {
        try {
            LostPostRequestDto requestDto = objectMapper.readValue(metadataJson, LostPostRequestDto.class);
            LostPostResponseDto responseDto = lostPostService.updateLostPost(postId, requestDto, images,loginUser);
            return new RsData<>("200", "신고 게시글을 성공적으로 수정했습니다.", responseDto);
        } catch (JsonProcessingException e) {
            return new RsData<>("400", "잘못된 JSON 형식입니다.", null);
        }
    }

    // 실종 신고 게시글 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary = "실종 신고 게시글 삭제")
    public RsData<Void> deleteLostPost(@PathVariable(name = "postId") Long postId,@LoginUser Member loginUser) {
        lostPostService.deleteLostPost(postId,loginUser);
        return new RsData<>("200", "실종 신고 게시글을 성공적으로 삭제했습니다.");
    }

    // 모든 실종 신고 게시글 조회 (페이징 지원)
    @GetMapping
    @Operation(summary = "모든 실종 신고 게시글 조회 (페이징 지원)")
    public RsData<Page<LostPostResponseDto>> getAllLostPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<LostPostResponseDto> posts = lostPostService.getAllLostPosts(pageable);
        return new RsData<>("200", "실종 신고 게시글 목록을 성공적으로 호출했습니다.", posts);
    }

    // 실종 신고 게시글 상세 조회
    @GetMapping("/{postId}")
    @Operation(summary = "실종 신고 게시글 상세 조회")
    public RsData<LostPostResponseDto> getLostPostById(@PathVariable(name = "postId") Long postId) {
        LostPostResponseDto responseDto = lostPostService.getLostPostById(postId);
        return new RsData<>("200", "실종 신고 게시글을 성공적으로 조회했습니다.", responseDto);
    }

    // 특정 실종 신고글에 대한 제보글 목록 조회
    @GetMapping("/{postId}/find-posts")
    @Operation(summary = "특정 실종 신고글에 연결된 제보글 목록 조회")
    public RsData<Page<FindPostResponseDto>> getFindPostsByLostPost(
            @PathVariable(name = "postId") Long postId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FindPostResponseDto> findPosts = lostPostService.getFindPostsByLostId(postId, pageable);
        return new RsData<>("200", "특정 실종 신고글에 대한 제보글 목록을 성공적으로 호출했습니다.", findPosts);
    }
}
