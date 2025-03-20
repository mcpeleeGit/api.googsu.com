package com.googsu.api.service;

import com.googsu.api.domain.Blog;
import com.googsu.api.domain.Member;
import com.googsu.api.dto.BlogDto;
import com.googsu.api.dto.BlogRequestDto;
import com.googsu.api.repository.BlogRepository;
import com.googsu.api.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    private final BlogRepository blogRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BlogDto createBlog(BlogDto blogDto, Long memberId) {
        log.info("Creating blog with memberId: {}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        log.info("Found member: {}", member.getName());

        Blog blog = Blog.builder()
                .title(blogDto.getTitle())
                .content(blogDto.getContent())
                .member(member)
                .build();

        Blog savedBlog = blogRepository.save(blog);
        log.info("Blog created successfully with id: {}", savedBlog.getId());

        return BlogDto.from(savedBlog);
    }

    @Transactional
    public BlogDto updateBlog(Long blogId, BlogDto blogDto) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + blogId));

        blog.update(blogDto.getTitle(), blogDto.getContent());
        log.info("Blog updated successfully with id: {}", blogId);

        return BlogDto.from(blog);
    }

    @Transactional
    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + blogId));

        blog.delete();
        log.info("Blog deleted successfully with id: {}", blogId);
    }

    public Page<BlogDto> getAllBlogs(Pageable pageable) {
        log.info("Getting all blogs with pagination");
        return blogRepository.findAllNotDeleted(pageable)
                .map(BlogDto::from);
    }

    public Page<BlogDto> getMemberBlogs(Long memberId, Pageable pageable) {
        log.info("Getting blogs for memberId: {}", memberId);
        return blogRepository.findByMemberIdNotDeleted(memberId, pageable)
                .map(BlogDto::from);
    }

    public BlogDto getBlog(Long blogId) {
        log.info("Getting blog with id: {}", blogId);
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + blogId));
        return BlogDto.from(blog);
    }
}