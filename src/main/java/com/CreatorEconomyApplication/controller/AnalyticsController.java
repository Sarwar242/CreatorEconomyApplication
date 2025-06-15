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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {
    
    private final ContentService contentService;
    
    /**
     * Serve the analytics dashboard page
     */
    @GetMapping
    public String analyticsPage(Model model) {
        return "analytics";
    }
    
    /**
     * Get analytics data for a specific user and date range
     */
    @GetMapping("/api/data")
    @ResponseBody
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
    @GetMapping("/api/top-content")
    @ResponseBody
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
    @GetMapping("/api/platform-stats")
    @ResponseBody
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
    @GetMapping("/api/engagement-timeline")
    @ResponseBody
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
    @GetMapping("/api/status-distribution")
    @ResponseBody
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
        
        // Calculate total metrics
        long totalViews = content.stream().mapToLong(c -> c.getViewCount() != null ? c.getViewCount() : 0).sum();
        long totalLikes = content.stream().mapToLong(c -> c.getLikeCount() != null ? c.getLikeCount() : 0).sum();
        long totalComments = content.stream().mapToLong(c -> c.getCommentCount() != null ? c.getCommentCount() : 0).sum();
        long totalShares = content.stream().mapToLong(c -> c.getShareCount() != null ? c.getShareCount() : 0).sum();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalViews", totalViews);
        metrics.put("totalLikes", totalLikes);
        metrics.put("totalComments", totalComments);
        metrics.put("totalShares", totalShares);
        metrics.put("totalContent", content.size());
        metrics.put("averageViews", content.isEmpty() ? 0 : totalViews / content.size());
        metrics.put("engagementRate", totalViews > 0 ? ((double)(totalLikes + totalComments + totalShares) / totalViews) * 100 : 0);
        
        analytics.put("metrics", metrics);
        analytics.put("platformStats", calculatePlatformStats(content));
        analytics.put("statusStats", calculateStatusStats(content));
        analytics.put("timelineData", calculateDailyStats(content, days));
        analytics.put("topContent", getTopPerformingContent(content, 10));
        
        return analytics;
    }
    
    /**
     * Calculate platform statistics
     */
    private Map<String, Object> calculatePlatformStats(List<ContentResponse> content) {
        Map<String, Object> platformStats = new HashMap<>();
        
        Map<String, List<ContentResponse>> platformGroups = content.stream()
            .collect(Collectors.groupingBy(c -> c.getTargetPlatforms().toString()));
        
        platformGroups.forEach((platform, contentList) -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("count", contentList.size());
            stats.put("totalViews", contentList.stream().mapToLong(c -> c.getViewCount() != null ? c.getViewCount() : 0).sum());
            stats.put("totalLikes", contentList.stream().mapToLong(c -> c.getLikeCount() != null ? c.getLikeCount() : 0).sum());
            stats.put("averageViews", contentList.isEmpty() ? 0 : 
                contentList.stream().mapToLong(c -> c.getViewCount() != null ? c.getViewCount() : 0).sum() / contentList.size());
            
            platformStats.put(platform, stats);
        });
        
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
     * Calculate daily statistics for timeline
     */
    private Map<String, Object> calculateDailyStats(List<ContentResponse> content, int days) {
        Map<String, Map<String, Long>> dailyStats = new HashMap<>();
        
        // Initialize all days with zero values
        LocalDateTime currentDate = LocalDateTime.now().minusDays(days);
        for (int i = 0; i <= days; i++) {
            String dateKey = currentDate.plusDays(i).toLocalDate().toString();
            Map<String, Long> dayStats = new HashMap<>();
            dayStats.put("views", 0L);
            dayStats.put("likes", 0L);
            dayStats.put("comments", 0L);
            dayStats.put("shares", 0L);
            dayStats.put("posts", 0L);
            dailyStats.put(dateKey, dayStats);
        }
        
        // Aggregate actual data
        content.forEach(contentItem -> {
            String dateKey = contentItem.getCreatedAt().toLocalDate().toString();
            if (dailyStats.containsKey(dateKey)) {
                Map<String, Long> dayStats = dailyStats.get(dateKey);
                dayStats.put("views", dayStats.get("views") + (contentItem.getViewCount() != null ? contentItem.getViewCount() : 0));
                dayStats.put("likes", dayStats.get("likes") + (contentItem.getLikeCount() != null ? contentItem.getLikeCount() : 0));
                dayStats.put("comments", dayStats.get("comments") + (contentItem.getCommentCount() != null ? contentItem.getCommentCount() : 0));
                dayStats.put("shares", dayStats.get("shares") + (contentItem.getShareCount() != null ? contentItem.getShareCount() : 0));
                dayStats.put("posts", dayStats.get("posts") + 1);
            }
        });
        
        return Map.of("daily", dailyStats);
    }
    
    /**
     * Get top performing content
     */
    private List<ContentResponse> getTopPerformingContent(List<ContentResponse> content, int limit) {
        return content.stream()
            .sorted((a, b) -> {
                long aScore = (a.getViewCount() != null ? a.getViewCount() : 0) * 3 + 
                             (a.getLikeCount() != null ? a.getLikeCount() : 0) * 2 +
                             (a.getCommentCount() != null ? a.getCommentCount() : 0) * 5 +
                             (a.getShareCount() != null ? a.getShareCount() : 0) * 4;
                long bScore = (b.getViewCount() != null ? b.getViewCount() : 0) * 3 + 
                             (b.getLikeCount() != null ? b.getLikeCount() : 0) * 2 +
                             (b.getCommentCount() != null ? b.getCommentCount() : 0) * 5 +
                             (b.getShareCount() != null ? b.getShareCount() : 0) * 4;
                return Long.compare(bScore, aScore);
            })
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate engagement timeline
     */
    private Map<String, Object> calculateEngagementTimeline(List<ContentResponse> content) {
        Map<String, Object> timeline = new HashMap<>();
        
        // Group by date and calculate engagement metrics
        Map<String, List<ContentResponse>> dailyContent = content.stream()
            .collect(Collectors.groupingBy(c -> c.getCreatedAt().toLocalDate().toString()));
        
        Map<String, Double> dailyEngagementRate = new HashMap<>();
        Map<String, Long> dailyTotalEngagement = new HashMap<>();
        
        dailyContent.forEach((date, contentList) -> {
            long totalViews = contentList.stream().mapToLong(c -> c.getViewCount() != null ? c.getViewCount() : 0).sum();
            long totalEngagement = contentList.stream().mapToLong(c -> 
                (c.getLikeCount() != null ? c.getLikeCount() : 0) +
                (c.getCommentCount() != null ? c.getCommentCount() : 0) +
                (c.getShareCount() != null ? c.getShareCount() : 0)
            ).sum();
            
            double engagementRate = totalViews > 0 ? ((double) totalEngagement / totalViews) * 100 : 0;
            
            dailyEngagementRate.put(date, engagementRate);
            dailyTotalEngagement.put(date, totalEngagement);
        });
        
        timeline.put("engagementRate", dailyEngagementRate);
        timeline.put("totalEngagement", dailyTotalEngagement);
        
        return timeline;
    }
    
    /**
     * Get default analytics data (fallback)
     */
    private Map<String, Object> getDefaultAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalViews", 0);
        metrics.put("totalLikes", 0);
        metrics.put("totalComments", 0);
        metrics.put("totalShares", 0);
        metrics.put("totalContent", 0);
        metrics.put("averageViews", 0);
        metrics.put("engagementRate", 0.0);
        
        analytics.put("metrics", metrics);
        analytics.put("platformStats", Map.of());
        analytics.put("statusStats", Map.of());
        analytics.put("timelineData", Map.of("daily", Map.of()));
        analytics.put("topContent", List.of());
        
        return analytics;
    }
}