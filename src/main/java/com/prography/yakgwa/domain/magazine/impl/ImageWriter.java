package com.prography.yakgwa.domain.magazine.impl;

import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.magazine.entity.Image;
import com.prography.yakgwa.domain.magazine.entity.ImageType;
import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.global.meta.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.prography.yakgwa.domain.magazine.entity.ImageType.CONTENT;
import static com.prography.yakgwa.domain.magazine.entity.ImageType.THUMBNAIL;

@RequiredArgsConstructor
@ImplService
public class ImageWriter {
    private final AwsS3Util awsS3Util;
    private final ImageJpaRepository imageRepository;

    @Async
    public void write(Magazine magazine, MultipartFile thumbnail, MultipartFile contents) {
        String thumbnailUrl = awsS3Util.upload(thumbnail, magazine.getTitle());
        String contentsUrl = awsS3Util.upload(contents, magazine.getTitle());
        Image thumbImage = Image.builder().url(thumbnailUrl).magazine(magazine).type(THUMBNAIL).build();
        Image contentImage = Image.builder().url(contentsUrl).magazine(magazine).type(CONTENT).build();
        imageRepository.saveAll(List.of(thumbImage, contentImage));
    }
}
