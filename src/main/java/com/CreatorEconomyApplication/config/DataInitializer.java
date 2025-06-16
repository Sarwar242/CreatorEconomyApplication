package com.CreatorEconomyApplication.config;

import com.CreatorEconomyApplication.model.entity.Content;
import com.CreatorEconomyApplication.model.entity.User;
import com.CreatorEconomyApplication.model.enums.ContentStatus;
import com.CreatorEconomyApplication.model.enums.PlatformType;
import com.CreatorEconomyApplication.model.enums.UserStatus;
import com.CreatorEconomyApplication.repository.ContentRepository;
import com.CreatorEconomyApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing test data...");
        
        // Create test user
        /* User user = new User("testuser", "test@example.com", "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi"); // password
        user.setRole("USER");
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        
        // Create test content
        createTestContent(user);
         */
        log.info("Test data initialized successfully!");
    }
    
    private void createTestContent(User user) {
        List<Content> contents = Arrays.asList(
            createContent(user, "My First Video", "A great video about coding", 
                         ContentStatus.PUBLISHED, Arrays.asList(PlatformType.YOUTUBE), 1000L, 150L, 25L, 10L),
            createContent(user, "Instagram Post", "Beautiful sunset photo", 
                         ContentStatus.PUBLISHED, Arrays.asList(PlatformType.INSTAGRAM), 500L, 75L, 15L, 5L),
            createContent(user, "TikTok Dance", "Fun dance challenge", 
                         ContentStatus.PUBLISHED, Arrays.asList(PlatformType.TIKTOK), 2000L, 300L, 50L, 20L),
            createContent(user, "Twitter Thread", "Thoughts on technology", 
                         ContentStatus.DRAFT, Arrays.asList(PlatformType.TWITTER), 0L, 0L, 0L, 0L),
            createContent(user, "Scheduled Post", "Coming soon...", 
                         ContentStatus.SCHEDULED, Arrays.asList(PlatformType.INSTAGRAM, PlatformType.FACEBOOK), 0L, 0L, 0L, 0L)
        );
        
        contentRepository.saveAll(contents);
    }
    
    private Content createContent(User user, String title, String description, 
                                ContentStatus status, List<PlatformType> platforms, 
                                Long views, Long likes, Long comments, Long shares) {
        Content content = new Content();
        content.setTitle(title);
        content.setDescription(description);
        content.setStatus(status);
        content.setTargetPlatforms(platforms);
        content.setUser(user);
        content.setViewCount(views);
        content.setLikeCount(likes);
        content.setCommentCount(comments);
        content.setShareCount(shares);
        content.setCreatedAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        
        if (status == ContentStatus.PUBLISHED) {
            content.setPublishedTime(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
        } else if (status == ContentStatus.SCHEDULED) {
            content.setScheduledPublishTime(LocalDateTime.now().plusDays(7));
        }
        
        return content;
    }
} 