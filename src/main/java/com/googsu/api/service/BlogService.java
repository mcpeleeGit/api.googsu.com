package com.googsu.api.service;

import com.googsu.api.domain.Blog;
import com.googsu.api.domain.Member;
import com.googsu.api.dto.BlogDto;
import com.googsu.api.dto.BlogRequestDto;
import com.googsu.api.repository.BlogRepository;
import com.googsu.api.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BlogDto createBlog(Long memberId, BlogRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Blog blog = new Blog();
        blog.setTitle(requestDto.getTitle());
        blog.setContent(requestDto.getContent());
        blog.setMember(member);

        Blog savedBlog = blogRepository.save(blog);
        return convertToDto(savedBlog);
    }

    @Transactional
    public BlogDto updateBlog(Long blogId, Long memberId, BlogRequestDto requestDto) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("블로그를 찾을 수 없습니다."));

        if (!blog.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("해당 블로그를 수정할 권한이 없습니다.");
        }

        blog.setTitle(requestDto.getTitle());
        blog.setContent(requestDto.getContent());

        Blog updatedBlog = blogRepository.save(blog);
        return convertToDto(updatedBlog);
    }

    @Transactional
    public void deleteBlog(Long blogId, Long memberId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("블로그를 찾을 수 없습니다."));

        if (!blog.getMember().getId().equals(memberId)) {
            throw new IllegalStateException("해당 블로그를 삭제할 권한이 없습니다.");
        }

        blog.setDeleted(true);
        blogRepository.save(blog);
    }

    public Page<BlogDto> getAllBlogs(Pageable pageable) {
        return blogRepository.findAllNotDeleted(pageable)
                .map(this::convertToDto);
    }

    public Page<BlogDto> getMemberBlogs(Long memberId, Pageable pageable) {
        return blogRepository.findByMemberIdNotDeleted(memberId, pageable)
                .map(this::convertToDto);
    }

    public BlogDto getBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("블로그를 찾을 수 없습니다."));
        return convertToDto(blog);
    }

    private BlogDto convertToDto(Blog blog) {
        BlogDto dto = new BlogDto();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setAuthorName(blog.getMember().getName());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setUpdatedAt(blog.getUpdatedAt());
        return dto;
    }
}