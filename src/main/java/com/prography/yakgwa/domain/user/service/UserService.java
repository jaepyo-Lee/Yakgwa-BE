package com.prography.yakgwa.domain.user.service;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.magazine.impl.ImageWriter;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.impl.UserReader;
import com.prography.yakgwa.global.format.exception.param.MultipartParamException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserReader userReader;
    private final AwsS3Util awsS3Util;

    public void modify(MultipartFile userImage, Long userId) throws IOException {
        if(userImage.isEmpty()){
            throw new MultipartParamException();
        }
        User user = userReader.read(userId);
        String upload = awsS3Util.upload(userImage, user.getAuthId() + user.getName());
        user.changeImage(upload);
    }

    public User find(Long userId) {
        return userReader.read(userId);
    }
}
