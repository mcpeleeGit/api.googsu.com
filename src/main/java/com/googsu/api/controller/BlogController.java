package com.googsu.api.controller;

import com.googsu.api.dto.BlogDto;
import com.googsu.api.dto.BlogRequestDto;
import com.googsu.api.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogDto> createBlog(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody BlogRequestDto requestDto) {
        return ResponseEntity.ok(blogService.createBlog(memberId, requestDto));
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<BlogDto> updateBlog(
            @PathVariable Long blogId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody BlogRequestDto requestDto) {
        return ResponseEntity.ok(blogService.updateBlog(blogId, memberId, requestDto));
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(
            @PathVariable Long blogId,
            @AuthenticationPrincipal Long memberId) {
        blogService.deleteBlog(blogId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<BlogDto>> getAllBlogs(Pageable pageable) {
        return ResponseEntity.ok(blogService.getAllBlogs(pageable));
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<BlogDto> getBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(blogService.getBlog(blogId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<BlogDto>> getMemberBlogs(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseEntity.ok(blogService.getMemberBlogs(memberId, pageable));
    }
}