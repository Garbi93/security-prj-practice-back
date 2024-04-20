package com.example.securityprjpracticeback.controller;

import com.example.securityprjpracticeback.dto.ProductDTO;
import com.example.securityprjpracticeback.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {
        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadedFileNames(uploadedFileNames);

        log.info(uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }



}
