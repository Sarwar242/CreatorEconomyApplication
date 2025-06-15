package com.CreatorEconomyApplication.controller;

import com.CreatorEconomyApplication.model.dto.request.ContentRequest;
import com.CreatorEconomyApplication.model.dto.response.ContentResponse;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;
import com.CreatorEconomyApplication.service.interfaces.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ContentController {
    
    private final ContentService contentService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        try {
            // Get recent content for dashboard
            Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
            Page<ContentResponse> recentContent = contentService.getRecentContent(pageable);
            
            // Get popular content
            Page<ContentResponse> popularContent = contentService.getMostPopularContent(pageable);
            
            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("recentContent", recentContent.getContent());
            dashboardData.put("popularContent", popularContent.getContent());
            
            return ResponseEntity.ok(dashboardData);
            
        } catch (Exception e) {
            log.error("Error loading dashboard data", e);
            return ResponseEntity.ok(Map.of());
        }
    }
    
    @PostMapping
    public ResponseEntity<ContentResponse> createContent(@Valid @RequestBody ContentRequest request) {
        log.info("Creating new content with title: {}", request.getTitle());
        ContentResponse response = contentService.createContent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentRequest request) {
        log.info("Updating content with id: {}", id);
        ContentResponse response = contentService.updateContent(id, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        ContentResponse response = contentService.getContentById(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        log.info("Deleting content with id: {}", id);
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    public ResponseEntity<Page<ContentResponse>> getAllContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ContentResponse> response = contentService.getAllContent(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ContentResponse>> getContentByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ContentResponse> response = contentService.getContentByUser(userId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ContentResponse>> getContentByStatus(
            @PathVariable ContentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.getContentByStatus(status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Page<ContentResponse>> getContentByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable ContentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.getContentByUserAndStatus(userId, status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/platform/{platform}")
    public ResponseEntity<Page<ContentResponse>> getContentByPlatform(
            @PathVariable PlatformType platform,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.getContentByPlatform(platform, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}/platform/{platform}")
    public ResponseEntity<Page<ContentResponse>> getContentByUserAndPlatform(
            @PathVariable Long userId,
            @PathVariable PlatformType platform,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.getContentByUserAndPlatform(userId, platform, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<ContentResponse>> searchContent(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.searchContent(keyword, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<Page<ContentResponse>> getMostPopularContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentResponse> response = contentService.getMostPopularContent(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<Page<ContentResponse>> getRecentContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContentResponse> response = contentService.getRecentContent(pageable);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/schedule")
    public ResponseEntity<ContentResponse> scheduleContent(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {
        
        ContentResponse response = contentService.scheduleContent(id, scheduledTime);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/scheduled/ready")
    public ResponseEntity<List<ContentResponse>> getScheduledContentReadyForPublishing() {
        List<ContentResponse> response = contentService.getScheduledContentReadyForPublishing();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/publish-scheduled")
    public ResponseEntity<ContentResponse> publishScheduledContent(@PathVariable Long id) {
        ContentResponse response = contentService.publishScheduledContent(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/analytics")
    public ResponseEntity<ContentResponse> updateContentAnalytics(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Long viewCount,
            @RequestParam(defaultValue = "0") Long likeCount,
            @RequestParam(defaultValue = "0") Long commentCount,
            @RequestParam(defaultValue = "0") Long shareCount) {
        
        ContentResponse response = contentService.updateContentAnalytics(
            id, viewCount, likeCount, commentCount, shareCount);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<ContentResponse> changeContentStatus(
            @PathVariable Long id,
            @RequestParam ContentStatus status) {
        
        ContentResponse response = contentService.changeContentStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/archive")
    public ResponseEntity<ContentResponse> archiveContent(@PathVariable Long id) {
        ContentResponse response = contentService.archiveContent(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/publish")
    public ResponseEntity<ContentResponse> publishContent(@PathVariable Long id) {
        ContentResponse response = contentService.publishContent(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/count/user/{userId}/status/{status}")
    public ResponseEntity<Long> getContentCountByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable ContentStatus status) {
        
        Long count = contentService.getContentCountByUserAndStatus(userId, status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<Page<ContentResponse>> getContentByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ContentResponse> response = contentService.getContentByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(response);
    }
}