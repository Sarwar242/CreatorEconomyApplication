package com.CreatorEconomyApplication.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CreatorEconomyApplication.model.entity.Content;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    // Find content by user ID
    Page<Content> findByUserId(Long userId, Pageable pageable);
    
    // Find content by status
    Page<Content> findByStatus(ContentStatus status, Pageable pageable);
    
    // Find content by user ID and status
    Page<Content> findByUserIdAndStatus(Long userId, ContentStatus status, Pageable pageable);
    
    // Find scheduled content that needs to be published
    @Query("SELECT c FROM Content c WHERE c.status = 'SCHEDULED' AND c.scheduledPublishTime <= :now")
    List<Content> findScheduledContentReadyForPublishing(@Param("now") LocalDateTime now);
    
    // Find content by platform
    @Query("SELECT c FROM Content c JOIN c.targetPlatforms p WHERE p = :platform")
    Page<Content> findByTargetPlatform(@Param("platform") PlatformType platform, Pageable pageable);
    
    // Find content by user and platform
    @Query("SELECT c FROM Content c JOIN c.targetPlatforms p WHERE c.user.id = :userId AND p = :platform")
    Page<Content> findByUserIdAndTargetPlatform(@Param("userId") Long userId, 
                                               @Param("platform") PlatformType platform, 
                                               Pageable pageable);
    
    // Search content by title or description
    @Query("SELECT c FROM Content c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Content> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Find most popular content by views
    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' ORDER BY c.viewCount DESC")
    Page<Content> findMostPopularContent(Pageable pageable);
    
    // Find recent content
    @Query("SELECT c FROM Content c WHERE c.status = 'PUBLISHED' ORDER BY c.publishedTime DESC")
    Page<Content> findRecentContent(Pageable pageable);
    
    // Get content statistics for a user
    @Query("SELECT COUNT(c) FROM Content c WHERE c.user.id = :userId AND c.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ContentStatus status);
    
    // Find content created within date range
    @Query("SELECT c FROM Content c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    Page<Content> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate, 
                                        Pageable pageable);
}