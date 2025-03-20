package com.googsu.api.controller;

import com.googsu.api.domain.Member;
import com.googsu.api.dto.BlogDto;
import com.googsu.api.dto.BlogRequestDto;
import com.googsu.api.service.BlogService;
import com.googsu.api.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@RequestBody BlogDto blogDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Creating blog for user: {}", userDetails.getUsername());
        Member member = memberService.getMemberByEmail(userDetails.getUsername());
        if (member == null) {
            throw new EntityNotFoundException("Member not found with email: " + userDetails.getUsername());
        }
        BlogDto createdBlog = blogService.createBlog(blogDto, member.getId());
        return ResponseEntity.ok(createdBlog);
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<BlogDto> updateBlog(@PathVariable Long blogId,
            @RequestBody BlogDto blogDto) {
        BlogDto updatedBlog = blogService.updateBlog(blogId, blogDto);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long blogId) {
        blogService.deleteBlog(blogId);
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