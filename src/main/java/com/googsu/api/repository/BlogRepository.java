package com.googsu.api.repository;

import com.googsu.api.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE b.isDeleted = false ORDER BY b.createdAt DESC")
    Page<Blog> findAllNotDeleted(Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.member.id = :memberId AND b.isDeleted = false ORDER BY b.createdAt DESC")
    Page<Blog> findByMemberIdNotDeleted(@Param("memberId") Long memberId, Pageable pageable);
}