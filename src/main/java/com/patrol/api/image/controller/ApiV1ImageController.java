package com.patrol.api.image.controller;

import com.patrol.domain.image.service.ImageService;
import com.patrol.domain.member.member.entity.Member;
import com.patrol.global.rsData.RsData;
import com.patrol.global.webMvc.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@Tag(name = "이미지 API", description = "사진 (Images)")
public class ApiV1ImageController {

  private final ImageService imageService;

  @DeleteMapping("/by-url")
  @Operation(summary = "이미지 URL로 삭제", description = "이미지 URL로 이미지를 삭제합니다.")
  public RsData<Void> deleteImageByUrl(
      @RequestParam String imageUrl,
      @LoginUser Member loginUser
  ) {
    imageService.deleteImage(imageUrl, loginUser.getId());
    return new RsData<>("200", "이미지가 성공적으로 삭제되었습니다.");
  }
}
