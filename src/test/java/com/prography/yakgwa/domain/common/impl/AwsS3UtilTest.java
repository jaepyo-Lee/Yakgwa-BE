package com.prography.yakgwa.domain.common.impl;

import com.prography.yakgwa.global.config.AwsS3TestConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(AwsS3TestConfig.class)
@SpringBootTest
class AwsS3UtilTest {

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private AwsS3Util awsS3Util;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    void AWS에파일정상업드르() throws IOException {
        // given
        String path = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());

        // when
        String urlPath = awsS3Util.upload(file, dirName);

        // then
        assertThat(urlPath).contains(path);
        assertThat(urlPath).contains(dirName);

    }

}