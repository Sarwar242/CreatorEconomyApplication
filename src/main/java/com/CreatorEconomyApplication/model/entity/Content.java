package com.CreatorEconomyApplication.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "content_url")
    private String contentUrl;
    
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentStatus status;
    
    @ElementCollection(targetClass = PlatformType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "content_platforms", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "platform_type")
    private List<PlatformType> targetPlatforms;
    
    @Column(name = "scheduled_publish_time")
    private LocalDateTime scheduledPublishTime;
    
    @Column(name = "published_time")
    private LocalDateTime publishedTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "like_count")
    private Long likeCount = 0L;
    
    @Column(name = "comment_count")
    private Long commentCount = 0L;
    
    @Column(name = "share_count")
    private Long shareCount = 0L;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}