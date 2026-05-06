package com.example.crypto_platform.user;

import com.example.crypto_platform.common.exception.ResourseNotFoundException;
import com.example.crypto_platform.user.entity.TelegramConversationState;
import com.example.crypto_platform.user.entity.User;
import com.example.crypto_platform.user.entity.UserRole;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository repository){
        this.userRepository=repository;
    }

    public User findOrCreateTelegramUser(Long telegramUserId, Long telegramChatId, String telegramUserName ){
        return userRepository.findByTelegramUserId(telegramUserId)
                .map(user->updateTelegramUser(user,telegramUserId,telegramChatId,telegramUserName))
                .orElseGet(()->createTelegramUser(telegramUserId,telegramChatId,telegramUserName));
    }

    public User findByUserId(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new ResolutionException("User not found"));
    }

    private User createTelegramUser(Long telegramUserId,Long telegramChatId, String telegramUserName){
        User user=new User();
        user.setTelegramUserId(telegramUserId);
        user.setTelegramChatId(telegramChatId);
        user.setTelegramUserName(telegramUserName);
        user.setUserRole(UserRole.USER);
        user.setConversationState(TelegramConversationState.IDLE);
        user.setEmailVerified(false);
        return userRepository.save(user);
    }
    public User findUserByTelegramId(Long id){
        return userRepository.findByTelegramUserId(id).orElseThrow(()->new ResourseNotFoundException("user not found"));
    }

    private User updateTelegramUser(User user,Long telegramUserId,Long telegramChatId, String telegramUserName){
        boolean changed=false;

        if (!telegramChatId.equals(user.getTelegramChatId())) {
            user.setTelegramChatId(telegramChatId);
            changed = true;
        }

        if (telegramUserName != null && !telegramUserName.equals(user.getTelegramUserName())) {
            user.setTelegramUserName(telegramUserName);
            changed = true;
        }

        if (changed) {
            return userRepository.save(user);
        }

        return user;
    }

    public User save(User user){
        return userRepository.save(user);
    }
}
