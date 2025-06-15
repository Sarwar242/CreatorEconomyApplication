package com.CreatorEconomyApplication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.CreatorEconomyApplication.exception.CreatorEconomyException;
import com.CreatorEconomyApplication.model.dto.request.ContentRequest;
import com.CreatorEconomyApplication.model.dto.response.ContentResponse;
import com.CreatorEconomyApplication.model.entity.Content;
import com.CreatorEconomyApplication.model.entity.User;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;
import com.CreatorEconomyApplication.repository.ContentRepository;
import com.CreatorEconomyApplication.repository.UserRepository;
import com.CreatorEconomyApplication.service.interfaces.ContentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContentServiceImpl implements ContentService {
    
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    
    @Override
    public ContentResponse createContent(ContentRequest request) {
        log.info("Creating new content with title: {}", request.getTitle());
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CreatorEconomyException("User not found with id: " + request.getUserId()));
        
        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContentUrl(request.getContentUrl());
        content.setThumbnailUrl(request.getThumbnailUrl());
        content.setStatus(request.getStatus());
        content.setTargetPlatforms(request.getTargetPlatforms());
        content.setScheduledPublishTime(request.getScheduledPublishTime());
        content.setUser(user);
        
        Content savedContent = contentRepository.save(content);
        log.info("Content created successfully with id: {}", savedContent.getId());
        
        return mapToResponse(savedContent);
    }
    
    @Override
    public ContentResponse updateContent(Long id, ContentRequest request) {
        log.info("Updating content with id: {}", id);
        
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContentUrl(request.getContentUrl());
        content.setThumbnailUrl(request.getThumbnailUrl());
        content.setStatus(request.getStatus());
        content.setTargetPlatforms(request.getTargetPlatforms());
        content.setScheduledPublishTime(request.getScheduledPublishTime());
        
        Content updatedContent = contentRepository.save(content);
        log.info("Content updated successfully with id: {}", updatedContent.getId());
        
        return mapToResponse(updatedContent);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ContentResponse getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        return mapToResponse(content);
    }
    
    @Override
    public void deleteContent(Long id) {
        log.info("Deleting content with id: {}", id);
        
        if (!contentRepository.existsById(id)) {
            throw new CreatorEconomyException("Content not found with id: " + id);
        }
        
        contentRepository.deleteById(id);
        log.info("Content deleted successfully with id: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getAllContent(Pageable pageable) {
        return contentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByUser(Long userId, Pageable pageable) {
        return contentRepository.findByUserId(userId, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByStatus(ContentStatus status, Pageable pageable) {
        return contentRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByUserAndStatus(Long userId, ContentStatus status, Pageable pageable) {
        return contentRepository.findByUserIdAndStatus(userId, status, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByPlatform(PlatformType platform, Pageable pageable) {
        return contentRepository.findByTargetPlatform(platform, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByUserAndPlatform(Long userId, PlatformType platform, Pageable pageable) {
        return contentRepository.findByUserIdAndTargetPlatform(userId, platform, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> searchContent(String keyword, Pageable pageable) {
        return contentRepository.searchByKeyword(keyword, pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getMostPopularContent(Pageable pageable) {
        return contentRepository.findMostPopularContent(pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getRecentContent(Pageable pageable) {
        return contentRepository.findRecentContent(pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    public ContentResponse scheduleContent(Long id, LocalDateTime scheduledTime) {
        log.info("Scheduling content with id: {} for time: {}", id, scheduledTime);
        
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        
        content.setScheduledPublishTime(scheduledTime);
        content.setStatus(ContentStatus.SCHEDULED);
        
        Content updatedContent = contentRepository.save(content);
        log.info("Content scheduled successfully with id: {}", updatedContent.getId());
        
        return mapToResponse(updatedContent);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ContentResponse> getScheduledContentReadyForPublishing() {
        List<Content> scheduledContent = contentRepository.findScheduledContentReadyForPublishing(LocalDateTime.now());
        return scheduledContent.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public ContentResponse publishScheduledContent(Long id) {
        log.info("Publishing scheduled content with id: {}", id);
        
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        
        content.setStatus(ContentStatus.PUBLISHED);
        content.setPublishedTime(LocalDateTime.now());
        
        Content publishedContent = contentRepository.save(content);
        log.info("Content published successfully with id: {}", publishedContent.getId());
        
        return mapToResponse(publishedContent);
    }
    
    @Override
    public ContentResponse updateContentAnalytics(Long id, Long viewCount, Long likeCount, 
                                                Long commentCount, Long shareCount) {
        log.info("Updating analytics for content with id: {}", id);
        
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        
        content.setViewCount(viewCount);
        content.setLikeCount(likeCount);
        content.setCommentCount(commentCount);
        content.setShareCount(shareCount);
        
        Content updatedContent = contentRepository.save(content);
        log.info("Analytics updated successfully for content with id: {}", updatedContent.getId());
        
        return mapToResponse(updatedContent);
    }
    
    @Override
    public ContentResponse changeContentStatus(Long id, ContentStatus status) {
        log.info("Changing status of content with id: {} to: {}", id, status);
        
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new CreatorEconomyException("Content not found with id: " + id));
        
        content.setStatus(status);
        if (status == ContentStatus.PUBLISHED) {
            content.setPublishedTime(LocalDateTime.now());
        }
        
        Content updatedContent = contentRepository.save(content);
        log.info("Status changed successfully for content with id: {}", updatedContent.getId());
        
        return mapToResponse(updatedContent);
    }
    
    @Override
    public ContentResponse archiveContent(Long id) {
        return changeContentStatus(id, ContentStatus.ARCHIVED);
    }
    
    @Override
    public ContentResponse publishContent(Long id) {
        return changeContentStatus(id, ContentStatus.PUBLISHED);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getContentCountByUserAndStatus(Long userId, ContentStatus status) {
        return contentRepository.countByUserIdAndStatus(userId, status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getContentByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return contentRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                .map(this::mapToResponse);
    }
    
    private ContentResponse mapToResponse(Content content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setDescription(content.getDescription());
        response.setContentUrl(content.getContentUrl());
        response.setThumbnailUrl(content.getThumbnailUrl());
        response.setStatus(content.getStatus());
        response.setTargetPlatforms(content.getTargetPlatforms());
        response.setScheduledPublishTime(content.getScheduledPublishTime());
        response.setPublishedTime(content.getPublishedTime());
        response.setUserName(content.getUser().getUsername());
        response.setViewCount(content.getViewCount());
        response.setLikeCount(content.getLikeCount());
        response.setCommentCount(content.getCommentCount());
        response.setShareCount(content.getShareCount());
        response.setCreatedAt(content.getCreatedAt());
        response.setUpdatedAt(content.getUpdatedAt());
        return response;
    }
}