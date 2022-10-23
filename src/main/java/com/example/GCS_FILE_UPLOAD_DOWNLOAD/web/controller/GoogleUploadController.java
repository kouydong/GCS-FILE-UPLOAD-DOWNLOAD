package com.example.GCS_FILE_UPLOAD_DOWNLOAD.web.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GoogleUploadController {

    @PostMapping(value = "/testload")
    public void uploadGoogleStorage(@RequestParam("fileName") MultipartFile file) throws Exception {
        /** 클래스 패스 경로 입니다.
         * 구글에서 제공하는 Json파일의 클래스 패스이며 기본적으로 classpath 시작점은 resources 폴더입니다.
         * 프로젝트에 맞게 설정 부탁 드립니다.
         */
        String classpath = "classpath:gcpcloud";

        /** 개발서버 프로젝트 아이디 입니다.
         * 개발 / 운영 프로젝트 아이디가 상이하므로 환경 설정파일(e.g *.properties or *.yaml 등)에 등록 하여 사용하시기 바랍니다.
         */
        String projectId = "apti-dev";

        /** 개발서버 인증키 파일 이름입니다.*/
        String authorizationFileName = "gcs-dev.json";

        /** 개발서버 버킷 이름입니다. GCS 버킷이란 일종의 가상 디렉토리 개념으로 보시면 됩니다.
         * 개발 / 운영 프로젝트 아이디가 상이하므로 환경 설정파일(e.g *.properties or *.yaml 등)에 등록 하여 사용하시기 바랍니다.
         */
        String bucketName = "apti-dev";

        /** JSON 파일 가져오기 */
        InputStream inputStream = ResourceUtils.getURL(classpath + "/" + authorizationFileName).openStream();

        /** GCS Credentials */
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
                .getService();

        /** 파일경로설정 */
        String filePath = "care";

        /** 파일이름 Renaming policy */
        String now = new SimpleDateFormat("yyyyMMddHHmmS").format(new Date());
        String fileName = now + "_" + file.getOriginalFilename();

        /** GCS 객체 아이디 생성 */
        BlobId blobId = BlobId.of(bucketName, filePath + "/" + fileName);

        /** GCC 객체 아이디 정보로 BlobInfo 정보 객체 */
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        /** GSC스토리지 저장 */
        Blob blob = storage.createFrom(blobInfo, file.getInputStream());
    }
}
