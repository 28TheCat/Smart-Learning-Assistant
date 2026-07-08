package com.wyt.controller;

import com.wyt.Utils.AliyunOSSOperator;
import com.wyt.exception.ErrorCode;
import com.wyt.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    private static final int MAX_FILENAME_LENGTH = 255;

    private static final Map<String, Set<String>> ALLOWED_CONTENT_TYPES_BY_EXTENSION = Map.of(
            "jpg", Set.of("image/jpeg"),
            "jpeg", Set.of("image/jpeg"),
            "png", Set.of("image/png"),
            "gif", Set.of("image/gif"),
            "webp", Set.of("image/webp")
    );

    private static final Set<String> DANGEROUS_INNER_EXTENSIONS = Set.of(
            "asp", "aspx", "bat", "cmd", "com", "exe", "html", "htm",
            "jar", "js", "jsp", "jspx", "php", "phtml", "sh", "svg"
    );

    private final AliyunOSSOperator aliyunOSSOperator;

    public UploadController(AliyunOSSOperator aliyunOSSOperator) {
        this.aliyunOSSOperator = aliyunOSSOperator;
    }

    @PostMapping
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        log.info("接收上传文件：name={}, size={}, contentType={}",
                sanitizeForLog(file == null ? null : file.getOriginalFilename()),
                file == null ? null : file.getSize(),
                file == null ? null : file.getContentType());

        UploadFileValidation validation = validate(file);
        if (!validation.valid()) {
            return Result.error(ErrorCode.PARAM_ERROR, validation.message());
        }

        String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + "." + validation.extension();
        String url = aliyunOSSOperator.upload(validation.content(), uniqueFileName);

        return Result.success(url);
    }

    private UploadFileValidation validate(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() <= 0) {
            return UploadFileValidation.invalid("上传文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            return UploadFileValidation.invalid("上传文件大小不能超过10MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            return UploadFileValidation.invalid("上传文件名不能为空");
        }

        String filename = originalFilename.trim();
        if (filename.length() > MAX_FILENAME_LENGTH || containsUnsafeFilenameCharacter(filename)) {
            return UploadFileValidation.invalid("上传文件名不合法");
        }

        if (filename.contains("/") || filename.contains("\\")) {
            return UploadFileValidation.invalid("上传文件名不合法");
        }

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex <= 0 || dotIndex == filename.length() - 1) {
            return UploadFileValidation.invalid("上传文件必须包含有效扩展名");
        }

        String baseName = filename.substring(0, dotIndex);
        if (!StringUtils.hasText(baseName)) {
            return UploadFileValidation.invalid("上传文件必须包含有效扩展名");
        }

        String extension = filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
        Set<String> allowedContentTypes = ALLOWED_CONTENT_TYPES_BY_EXTENSION.get(extension);
        if (allowedContentTypes == null) {
            return UploadFileValidation.invalid("仅支持上传 jpg、jpeg、png、gif、webp 图片");
        }

        if (containsDangerousInnerExtension(baseName)) {
            return UploadFileValidation.invalid("上传文件名包含不允许的扩展名");
        }

        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType)) {
            return UploadFileValidation.invalid("上传文件类型不能为空");
        }

        String normalizedContentType = contentType.toLowerCase(Locale.ROOT).split(";", 2)[0].trim();
        if (!allowedContentTypes.contains(normalizedContentType)) {
            return UploadFileValidation.invalid("上传文件类型与扩展名不匹配");
        }

        byte[] content;
        try {
            content = file.getBytes();
        } catch (Exception e) {
            log.warn("读取上传文件失败：name={}, size={}, contentType={}",
                    filename, file.getSize(), file.getContentType(), e);
            return UploadFileValidation.invalid("上传文件读取失败");
        }

        if (content.length == 0) {
            return UploadFileValidation.invalid("上传文件不能为空");
        }

        if (!matchesImageSignature(extension, content)) {
            return UploadFileValidation.invalid("上传文件内容不是有效图片");
        }

        return UploadFileValidation.valid(extension, content);
    }

    private boolean matchesImageSignature(String extension, byte[] content) {
        return switch (extension) {
            case "jpg", "jpeg" -> startsWith(content, new int[]{0xFF, 0xD8, 0xFF});
            case "png" -> startsWith(content, new int[]{0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A});
            case "gif" -> startsWith(content, "GIF87a".getBytes(StandardCharsets.US_ASCII))
                    || startsWith(content, "GIF89a".getBytes(StandardCharsets.US_ASCII));
            case "webp" -> content.length >= 12
                    && startsWith(content, "RIFF".getBytes(StandardCharsets.US_ASCII))
                    && content[8] == 'W'
                    && content[9] == 'E'
                    && content[10] == 'B'
                    && content[11] == 'P';
            default -> false;
        };
    }

    private boolean startsWith(byte[] content, int[] signature) {
        if (content.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if ((content[i] & 0xFF) != signature[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean startsWith(byte[] content, byte[] signature) {
        if (content.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if (content[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean containsUnsafeFilenameCharacter(String filename) {
        for (int i = 0; i < filename.length(); i++) {
            int type = Character.getType(filename.charAt(i));
            if (type == Character.CONTROL || type == Character.FORMAT) {
                return true;
            }
        }
        return false;
    }

    private String sanitizeForLog(String value) {
        if (value == null) {
            return null;
        }

        StringBuilder sanitized = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            int type = Character.getType(character);
            sanitized.append(type == Character.CONTROL || type == Character.FORMAT ? '?' : character);
        }
        return sanitized.toString();
    }

    private boolean containsDangerousInnerExtension(String baseName) {
        String[] parts = baseName.toLowerCase(Locale.ROOT).split("\\.");
        for (int i = 1; i < parts.length; i++) {
            if (DANGEROUS_INNER_EXTENSIONS.contains(parts[i])) {
                return true;
            }
        }
        return false;
    }

    private record UploadFileValidation(boolean valid, String extension, byte[] content, String message) {
        private static UploadFileValidation valid(String extension, byte[] content) {
            return new UploadFileValidation(true, extension, content, null);
        }

        private static UploadFileValidation invalid(String message) {
            return new UploadFileValidation(false, null, null, message);
        }
    }
}
