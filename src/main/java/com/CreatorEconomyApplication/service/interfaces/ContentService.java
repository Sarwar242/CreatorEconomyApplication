package com.CreatorEconomyApplication.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.CreatorEconomyApplication.model.dto.request.ContentRequest;
import com.CreatorEconomyApplication.model.dto.response.ContentResponse;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentService {
    
    // CRUD Operations
    ContentResponse createContent(ContentRequest request);
    ContentResponse updateContent(Long id, ContentRequest request);
    ContentResponse getContentById(Long id);
    void deleteContent(Long id);
    
    // Content Management
    Page<ContentResponse> getAllContent(Pageable pageable);
    Page<ContentResponse> getContentByUser(Long userId, Pageable pageable);
    Page<ContentResponse> getContentByStatus(ContentStatus status, Pageable pageable);
    Page<ContentResponse> getContentByUserAndStatus(Long userId, ContentStatus status, Pageable pageable);
    
    // Platform Management
    Page<ContentResponse> getContentByPlatform(PlatformType platform, Pageable pageable);
    Page<ContentResponse> getContentByUserAndPlatform(Long userId, PlatformType platform, Pageable pageable);
    
    // Search and Discovery
    Page<ContentResponse> searchContent(String keyword, Pageable pageable);
    Page<ContentResponse> getMostPopularContent(Pageable pageable);
    Page<ContentResponse> getRecentContent(Pageable pageable);
    
    // Scheduling
    ContentResponse scheduleContent(Long id, LocalDateTime scheduledTime);
    List<ContentResponse> getScheduledContentReadyForPublishing();
    ContentResponse publishScheduledContent(Long id);
    
    // Analytics Updates
    ContentResponse updateContentAnalytics(Long id, Long viewCount, Long likeCount, 
                                         Long commentCount, Long shareCount);
    
    // Status Management
    ContentResponse changeContentStatus(Long id, ContentStatus status);
    ContentResponse archiveContent(Long id);
    ContentResponse publishContent(Long id);
    
    // Statistics
    Long getContentCountByUserAndStatus(Long userId, ContentStatus status);
    Page<ContentResponse> getContentByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}