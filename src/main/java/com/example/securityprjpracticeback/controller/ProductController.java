package com.example.securityprjpracticeback.controller;

import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.ProductDTO;
import com.example.securityprjpracticeback.service.ProductService;
import com.example.securityprjpracticeback.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;

    private final ProductService productService;

//    @PostMapping("/")
//    public Map<String, String> register(ProductDTO productDTO) {
//        log.info("register: " + productDTO);
//
//        List<MultipartFile> files = productDTO.getFiles();
//
//        List<String> uploadedFileNames = fileUtil.saveFiles(files);
//
//        productDTO.setUploadFileNames(uploadedFileNames);
//
//        log.info(uploadedFileNames);
//
//        return Map.of("RESULT", "SUCCESS");
//    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    // 목록 데이터 조회
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    // 상풍 등록
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        Long pno = productService.register(productDTO);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    // 상품 수정
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        // old product -> 이미 저장된 상품 정보 받아오기
        ProductDTO oldProductDTO = productService.get(pno);

        // 이미지 업로드 저장
        List<MultipartFile> files = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // 수정 전 기존에 DB에 존재 하는 이미지 정보
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        // 새로 업로드될 파일이 있다면 기존 DB에 존재하는 정보에 새로 추가된 이미지 정보를 추가해주자
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        // 수정된 정보 저장
        productService.modify(productDTO);

        // 만약 기존에 등록된 이미지를 지우고 싶을때?
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();
        if (oldFileNames != null && oldFileNames.size() > 0) {

            // 기존 업로드 파일들 이름을 가져와서 새로 업로드될 파일들 이름을 순회하여 비교하며 없는 애들을 찾아 변수에 저장.
            List<String> removeFiles =
                    oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            // 없는 파일 정보는 지워준다.
            fileUtil.deleteFiles(removeFiles);

        }// end if

        return Map.of("RESULT", "SUCCESS");
    }

    // 상품 삭제
    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable Long pno) {
        // 기존 이미지 파일들을 삭제 하기 위해
        // 해당 상품 이미지 목록을 조회한다.
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        // 해당 상품 DB 삭제 하기
        productService.remove(pno);

        // 해당 상품 이미지 목록 삭제하기
        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }

}
