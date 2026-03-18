package com.ra.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class UploadService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException{
        // Upload và nhận kết quả trả về dưới dạng Map
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(), ObjectUtils.asMap("resource_type", "auto")
        );

        // Trả về đường dẫn URL của ảnh sau khi upload thành công
        return uploadResult.get("url").toString();
    }
}
