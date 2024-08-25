package com.prography.yakgwa.domain.user.service;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.global.format.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final AwsS3Util awsS3Util;
    private final UserJpaRepository userJpaRepository;

    @Value("${user.base.image}")
    private String baseImg;


    @Transactional
    public boolean updateFcm(Long userId, String newFcmToken) {
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        user.refreshFcm(newFcmToken);
        return true;
    }


    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 2024-07-30
     */
    public void modify(MultipartFile userImage, Long userId) throws IOException {
        User user = userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        if (userImage == null || userImage.isEmpty()) {
            user.changeImage(baseImg);
            return;
        }
        String upload = awsS3Util.upload(userImage, user.getAuthId() + user.getName());
        user.changeImage(upload);
    }

    /**
     * Work) Test Code
     * Write-Date) 2024-07-29, 월, 14:20
     * Finish-Date) 2024-07-30
     */
    public User find(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
