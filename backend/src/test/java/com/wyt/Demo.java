package com.wyt;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class Demo {

    public static void main(String[] args) throws Exception {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";

        String bucketName = "wangyating";

        String objectName = "images/001.jpg"; // 可以自定义路径，比如 "images/001.jpg"

        String region = "cn-beijing";

        EnvironmentVariableCredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 配置 V4 签名
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        //创建 OSS 客户端
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 本地文件路径
            File file = new File("F:\\Desktop\\Smart-Learning-Assistant\\src\\main\\resources\\scr\\9e1937db-ef78-4c83-9665-05b491c00f8e.png");
            byte[] content = Files.readAllBytes(file.toPath());

            // 上传文件
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
            System.out.println("✅ 上传成功！");

            // 打印访问 URL
            String url = "https://" + bucketName + ".oss-cn-beijing.aliyuncs.com/" + objectName;
            System.out.println("🌐 文件访问地址: " + url);

        } catch (OSSException oe) {
            System.err.println("❌ OSS 服务端异常：");
            System.err.println("Message: " + oe.getErrorMessage());
            System.err.println("Code: " + oe.getErrorCode());
            System.err.println("Request ID: " + oe.getRequestId());
            System.err.println("Host ID: " + oe.getHostId());
        } catch (ClientException ce) {
            System.err.println("❌ 客户端异常（网络/认证问题）：");
            System.err.println("Message: " + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
