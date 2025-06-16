package com.CreatorEconomyApplication.controller;

import com.CreatorEconomyApplication.model.dto.response.ContentResponse;
import com.CreatorEconomyApplication.service.interfaces.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST API Controller for Analytics endpoints
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnalyticsApiController {
    
    private final ContentService contentService;
    
    /**
     * Get analytics data for a specific user and date range
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getAnalyticsData(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "30") int days) {
        
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(days);
            
            // Get content for the specified date range
            Pageable pageable = PageRequest.of(0, 1000, Sort.by("createdAt").descending());
            Page<ContentResponse> contentPage;
            
            if (userId != null) {
                //contentPage = contentService.getContentByDateRangeAndUser(startDate, endDate, userId, pageable);
            } else {

            }
            contentPage = contentService.getContentByDateRange(startDate, endDate, pageable);
            List<ContentResponse> content = contentPage.getContent();
            
            // Calculate metrics
            Map<String, Object> analytics = calculateAnalytics(content, days);
            
            return ResponseEntity.ok(analytics);
            
        } catch (Exception e) {
            log.error("Error fetching analytics data", e);
            return ResponseEntity.ok(getDefaultAnalytics());
        }
    }
    
    /**
     * Get top performing content
     */
    @GetMapping("/top-content")
    public ResponseEntity<List<ContentResponse>> getTopContent(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            Pageable pageable = PageRequest.of(0, limit);
            Page<ContentResponse> topContent;
            
            if (userId != null) {
                //topContent = contentService.getMostPopularContentByUser(userId, pageable);
            } else {
               
            }
            topContent = contentService.getMostPopularContent(pageable);
            return ResponseEntity.ok(topContent.getContent());
            
        } catch (Exception e) {
            log.error("Error fetching top content", e);
            return ResponseEntity.ok(List.of());
        }
    }
    
    /**
     * Get platform distribution analytics
     */
    @GetMapping("/platform-stats")
    public ResponseEntity<Map<String, Object>> getPlatformStats(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "30") int days) {
        
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(days);
            
            Pageable pageable = PageRequest.of(0, 1000);
            Page<ContentResponse> contentPage;
            
            if (userId != null) {
                //contentPage = contentService.getContentByDateRangeAndUser(startDate, endDate, userId, pageable);
            } else {
               
            }
            contentPage = contentService.getContentByDateRange(startDate, endDate, pageable);
            Map<String, Object> platformStats = calculatePlatformStats(contentPage.getContent());
            
            return ResponseEntity.ok(platformStats);
            
        } catch (Exception e) {
            log.error("Error fetching platform stats", e);
            return ResponseEntity.ok(Map.of());
        }
    }
    
    /**
     * Get engagement metrics over time
     */
    @GetMapping("/engagement-timeline")
    public ResponseEntity<Map<String, Object>> getEngagementTimeline(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "30") int days) {
        
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(days);
            
            Pageable pageable = PageRequest.of(0, 1000, Sort.by("createdAt").ascending());
            Page<ContentResponse> contentPage;
            
            if (userId != null) {
               // contentPage = contentService.getContentByDateRangeAndUser(startDate, endDate, userId, pageable);
            } else {
               
            }
            contentPage = contentService.getContentByDateRange(startDate, endDate, pageable);
            Map<String, Object> timeline = calculateEngagementTimeline(contentPage.getContent());
            
            return ResponseEntity.ok(timeline);
            
        } catch (Exception e) {
            log.error("Error fetching engagement timeline", e);
            return ResponseEntity.ok(Map.of());
        }
    }
    
    /**
     * Get content status distribution
     */
    @GetMapping("/status-distribution")
    public ResponseEntity<Map<String, Integer>> getStatusDistribution(
            @RequestParam(required = false) Long userId) {
        
        try {
            Pageable pageable = PageRequest.of(0, 1000);
            Page<ContentResponse> contentPage;
            
            if (userId != null) {
                contentPage = contentService.getContentByUser(userId, pageable);
            } else {
                contentPage = contentService.getAllContent(pageable);
            }
            
            Map<String, Integer> statusDistribution = contentPage.getContent().stream()
                .collect(Collectors.groupingBy(
                    content -> content.getStatus().toString(),
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
            
            return ResponseEntity.ok(statusDistribution);
            
        } catch (Exception e) {
            log.error("Error fetching status distribution", e);
            return ResponseEntity.ok(Map.of());
        }
    }
    
    /**
     * Calculate comprehensive analytics from content list
     */
    private Map<String, Object> calculateAnalytics(List<ContentResponse> content, int days) {
        Map<String, Object> analytics = new HashMap<>();
        
        // Basic metrics
        analytics.put("totalContent", content.size());
        analytics.put("totalViews", content.stream().mapToLong(c -> c.getViewCount()).sum());
        analytics.put("totalLikes", content.stream().mapToLong(c -> c.getLikeCount()).sum());
        analytics.put("totalComments", content.stream().mapToLong(c -> c.getCommentCount()).sum());
        analytics.put("totalShares", content.stream().mapToLong(c -> c.getShareCount()).sum());
        
        // Average engagement
        if (!content.isEmpty()) {
            analytics.put("avgViews", content.stream().mapToLong(c -> c.getViewCount()).average().orElse(0));
            analytics.put("avgLikes", content.stream().mapToLong(c -> c.getLikeCount()).average().orElse(0));
            analytics.put("avgComments", content.stream().mapToLong(c -> c.getCommentCount()).average().orElse(0));
            analytics.put("avgShares", content.stream().mapToLong(c -> c.getShareCount()).average().orElse(0));
        }
        
        // Status distribution
        analytics.put("statusDistribution", calculateStatusStats(content));
        
        // Daily stats
        analytics.put("dailyStats", calculateDailyStats(content, days));
        
        // Top performing content
        analytics.put("topContent", getTopPerformingContent(content, 10));
        
        return analytics;
    }
    
    /**
     * Calculate platform statistics
     */
    private Map<String, Object> calculatePlatformStats(List<ContentResponse> content) {
        Map<String, Object> platformStats = new HashMap<>();
        
        // Platform distribution
        Map<String, Long> platformDistribution = content.stream()
            .flatMap(c -> c.getTargetPlatforms().stream())
            .collect(Collectors.groupingBy(
                platform -> platform.toString(),
                Collectors.counting()
            ));
        
        platformStats.put("platformDistribution", platformDistribution);
        
        // Engagement by platform
        Map<String, Map<String, Long>> engagementByPlatform = new HashMap<>();
        content.forEach(c -> {
            c.getTargetPlatforms().forEach(platform -> {
                String platformName = platform.toString();
                engagementByPlatform.computeIfAbsent(platformName, k -> new HashMap<>());
                
                Map<String, Long> stats = engagementByPlatform.get(platformName);
                stats.put("views", stats.getOrDefault("views", 0L) + c.getViewCount());
                stats.put("likes", stats.getOrDefault("likes", 0L) + c.getLikeCount());
                stats.put("comments", stats.getOrDefault("comments", 0L) + c.getCommentCount());
                stats.put("shares", stats.getOrDefault("shares", 0L) + c.getShareCount());
            });
        });
        
        platformStats.put("engagementByPlatform", engagementByPlatform);
        
        return platformStats;
    }
    
    /**
     * Calculate status statistics
     */
    private Map<String, Integer> calculateStatusStats(List<ContentResponse> content) {
        return content.stream()
            .collect(Collectors.groupingBy(
                c -> c.getStatus().toString(),
                Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
            ));
    }
    
    /**
     * Calculate daily statistics
     */
    private Map<String, Object> calculateDailyStats(List<ContentResponse> content, int days) {
        Map<String, Object> dailyStats = new HashMap<>();
        
        // Group content by date
        Map<String, List<ContentResponse>> contentByDate = content.stream()
            .collect(Collectors.groupingBy(
                c -> c.getCreatedAt().toLocalDate().toString()
            ));
        
        // Calculate daily metrics
        Map<String, Map<String, Object>> dailyMetrics = new HashMap<>();
        contentByDate.forEach((date, dayContent) -> {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("contentCount", dayContent.size());
            metrics.put("totalViews", dayContent.stream().mapToLong(c -> c.getViewCount()).sum());
            metrics.put("totalLikes", dayContent.stream().mapToLong(c -> c.getLikeCount()).sum());
            metrics.put("totalComments", dayContent.stream().mapToLong(c -> c.getCommentCount()).sum());
            metrics.put("totalShares", dayContent.stream().mapToLong(c -> c.getShareCount()).sum());
            
            dailyMetrics.put(date, metrics);
        });
        
        dailyStats.put("dailyMetrics", dailyMetrics);
        
        return dailyStats;
    }
    
    /**
     * Get top performing content
     */
    private List<ContentResponse> getTopPerformingContent(List<ContentResponse> content, int limit) {
        return content.stream()
            .sorted((a, b) -> Long.compare(b.getViewCount(), a.getViewCount()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate engagement timeline
     */
    private Map<String, Object> calculateEngagementTimeline(List<ContentResponse> content) {
        Map<String, Object> timeline = new HashMap<>();
        
        // Sort content by creation date
        List<ContentResponse> sortedContent = content.stream()
            .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
            .collect(Collectors.toList());
        
        // Calculate cumulative metrics
        long cumulativeViews = 0;
        long cumulativeLikes = 0;
        long cumulativeComments = 0;
        long cumulativeShares = 0;
        
        Map<String, Map<String, Long>> timelineData = new HashMap<>();
        
        for (ContentResponse c : sortedContent) {
            cumulativeViews += c.getViewCount();
            cumulativeLikes += c.getLikeCount();
            cumulativeComments += c.getCommentCount();
            cumulativeShares += c.getShareCount();
            
            Map<String, Long> point = new HashMap<>();
            point.put("views", cumulativeViews);
            point.put("likes", cumulativeLikes);
            point.put("comments", cumulativeComments);
            point.put("shares", cumulativeShares);
            
            timelineData.put(c.getCreatedAt().toString(), point);
        }
        
        timeline.put("timeline", timelineData);
        
        return timeline;
    }
    
    /**
     * Get default analytics when no data is available
     */
    private Map<String, Object> getDefaultAnalytics() {
        Map<String, Object> defaultAnalytics = new HashMap<>();
        defaultAnalytics.put("totalContent", 0);
        defaultAnalytics.put("totalViews", 0);
        defaultAnalytics.put("totalLikes", 0);
        defaultAnalytics.put("totalComments", 0);
        defaultAnalytics.put("totalShares", 0);
        defaultAnalytics.put("avgViews", 0.0);
        defaultAnalytics.put("avgLikes", 0.0);
        defaultAnalytics.put("avgComments", 0.0);
        defaultAnalytics.put("avgShares", 0.0);
        defaultAnalytics.put("statusDistribution", Map.of());
        defaultAnalytics.put("dailyStats", Map.of());
        defaultAnalytics.put("topContent", List.of());
        return defaultAnalytics;
    }
} 