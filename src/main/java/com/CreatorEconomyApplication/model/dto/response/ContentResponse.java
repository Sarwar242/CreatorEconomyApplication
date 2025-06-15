package com.CreatorEconomyApplication.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;
import lombok.Data;

@Data
public class ContentResponse {
    
    private Long id;
    private String title;
    private String description;
    private String contentUrl;
    private String thumbnailUrl;
    private ContentStatus status;
    private List<PlatformType> targetPlatforms;
    private LocalDateTime scheduledPublishTime;
    private LocalDateTime publishedTime;
    private String userName;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}