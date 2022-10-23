package com.example.GCS_FILE_UPLOAD_DOWNLOAD.web.controller;

import com.google.cloud.storage.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class GoogleUploadContorllerByADC {

    @PostMapping(value = "/test-upload")
    public void uploadGoogleStorage(@RequestParam("fileName") MultipartFile file) throws IOException {

        /** 파일이름 Default Renaming Policy 입니다.
         *  개발 시 별도의 Renaming policy 있으시면 당사 담당자와 확인해 주시기 부탁 드립니다.
         */
        String now = new SimpleDateFormat("yyyyMMddHHmmS").format(new Date());
        String fileName = now + "_" + file.getOriginalFilename();

        /** GCS(Google Cloud Storage 자격인증과 관련된 사항입니다.
         *  As is : Spring boot Application 내에 JSON 파일을 읽어서 자격인증
         *  To be : Google ADC(Authentication Default Credentials) 방식으로 변경
         *  설정과 관련된 자세한 사항은 https://blog.naver.com/kouydong/222907633638 참조 부탁 드립니다.
         */
         Storage storage = StorageOptions.getDefaultInstance().getService();

        /** 개발서버 버킷 이름입니다.
         * GCS 버킷이란 일종의 객체를 담는 Container 보시면됩니다.(일종의 Folder, Directory)
         */
        String bucketName = "apti-dev";

        /** 파일경로설정 */
        String filePath = "care";

        /** GCS 객체 아이디 생성 */
        BlobId blobId = BlobId.of(bucketName, filePath + "/" + fileName);

        /** GCC 객체 아이디 정보로 BlobInfo 정보 객체 */
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        /** GSC스토리지 저장 */
        Blob blob = storage.createFrom(blobInfo, file.getInputStream());

    }
}
