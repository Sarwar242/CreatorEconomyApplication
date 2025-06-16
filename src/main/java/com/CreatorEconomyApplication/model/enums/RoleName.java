package com.CreatorEconomyApplication.model.enums;

public enum RoleName {
    ROLE_USER("User"),
    ROLE_ADMIN("Administrator"),
    ROLE_MODERATOR("Moderator"),
    ROLE_MANAGER("Manager");
    
    private final String displayName;
    
    RoleName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}