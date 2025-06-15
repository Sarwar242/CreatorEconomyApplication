package com.CreatorEconomyApplication.model.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContentRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String contentUrl;
    
    private String thumbnailUrl;
    
    @NotNull(message = "Status is required")
    private ContentStatus status;
    
    private List<PlatformType> targetPlatforms;
    
    private LocalDateTime scheduledPublishTime;
    
    @NotNull(message = "User ID is required")
    private Long userId;
}
