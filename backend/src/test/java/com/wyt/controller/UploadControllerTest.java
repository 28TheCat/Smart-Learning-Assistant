package com.wyt.controller;

import com.wyt.Utils.AliyunOSSOperator;
import com.wyt.pojo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UploadControllerTest {

    private final AliyunOSSOperator aliyunOSSOperator = mock(AliyunOSSOperator.class);
    private final UploadController uploadController = new UploadController(aliyunOSSOperator);

    @Test
    void uploadRejectsMissingFile() throws Exception {
        Result result = uploadController.upload(null);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件不能为空");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", new byte[0]);

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件不能为空");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsBlankFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", "image/png", "image".getBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件名不能为空");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsFilenameWithPath() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "..\\avatar.png", "image/png", pngBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件名不合法");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsFilenameWithControlCharacter() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar\u0000.png", "image/png", pngBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件名不合法");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsTooLongFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "a".repeat(256) + ".png", "image/png", pngBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件名不合法");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsMissingExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar", "image/png", "image".getBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件必须包含有效扩展名");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsUnsupportedExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.exe", "application/octet-stream", "image".getBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("仅支持上传 jpg、jpeg、png、gif、webp 图片");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsDangerousInnerExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.php.png", "image/png", pngBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件名包含不允许的扩展名");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsMissingContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", null, pngBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件类型不能为空");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsMismatchedContentType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.jpg", "text/plain", "image".getBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件类型与扩展名不匹配");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsSpoofedImageContent() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", "not image".getBytes());

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件内容不是有效图片");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadRejectsOversizedFile() throws Exception {
        byte[] oversizedContent = new byte[10 * 1024 * 1024 + 1];
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", oversizedContent);

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isZero();
        assertThat(result.getErrorCode()).isEqualTo("PARAM_ERROR");
        assertThat(result.getMsg()).isEqualTo("上传文件大小不能超过10MB");
        verify(aliyunOSSOperator, never()).upload(any(), any());
    }

    @Test
    void uploadStoresAllowedImageWithGeneratedName() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.PNG", "image/png", pngBytes());
        when(aliyunOSSOperator.upload(any(), any())).thenReturn("https://example.com/avatar.png");

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isEqualTo("https://example.com/avatar.png");
        verify(aliyunOSSOperator).upload(any(), argThat(fileName -> fileName.matches("[a-f0-9]{32}\\.png")));
    }

    @Test
    void uploadAcceptsContentTypeWithParameters() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "IMAGE/PNG; charset=UTF-8", pngBytes());
        when(aliyunOSSOperator.upload(any(), any())).thenReturn("https://example.com/avatar.png");

        Result result = uploadController.upload(file);

        assertThat(result.getCode()).isEqualTo(1);
        assertThat(result.getData()).isEqualTo("https://example.com/avatar.png");
        verify(aliyunOSSOperator).upload(any(), argThat(fileName -> fileName.matches("[a-f0-9]{32}\\.png")));
    }

    private byte[] pngBytes() {
        return new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D
        };
    }
}
