package com.service.surveyservice.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@NoArgsConstructor
public class S3Config {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }


    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = dirName+"_"+ UUID.randomUUID();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getBytes().length);
        s3Client.putObject(new PutObjectRequest(bucket + "/" + dirName, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getObject(bucket + "/" + dirName, fileName).getKey();
    }

    public void delete(String fileName, String dirName) {

        s3Client.deleteObject(bucket + "/" + dirName, fileName);
    }

    public String getDefaultSurveyUrl(){
        return s3Client.getUrl(bucket+"/" + "survey", "survey_default.png").toString();
    }

    public String getDefaultOptionUrl(){
        return s3Client.getUrl(bucket+"/" + "survey", "option_default.png").toString();
    }
}