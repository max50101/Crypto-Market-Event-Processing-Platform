package com.example.crypto_platform.user;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="telegram_user_id",unique = true, nullable = false)
    private Long telegramUserId;

    @Column(name="telegram_chat_id", nullable = false)
    private Long telegramChatId;

    @Column(name="telegram_username")
    private String telegramUserName;

    @Enumerated(EnumType.STRING)
    @Column(name="user_role",nullable = false)
    private UserRole userRole=UserRole.USER;

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TelegramConversationState getConversationState() {
        return conversationState;
    }

    public void setConversationState(TelegramConversationState conversationState) {
        this.conversationState = conversationState;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getTelegramUserName() {
        return telegramUserName;
    }

    public void setTelegramUserName(String telegramUserName) {
        this.telegramUserName = telegramUserName;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="email")
    private String email;

    @Column(name="email_verified",nullable = false)
    private boolean emailVerified=false;

    @Enumerated(EnumType.STRING)
    @Column(name="conversation_state",nullable = false)
    private TelegramConversationState conversationState= TelegramConversationState.IDLE;

}
