package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {
    @Autowired
    private ProductService productService;

    // 목록 조회 테스트
    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());
    }

    // 상품 등록 테스트
    @Test
    public void testRegister() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품 입니다.")
                .price(1000)
                .build();

        // 등록될 상품의 이미지 추가 시 이미지에 uuid 적용
        productDTO.setUploadFileNames(
                java.util.List.of(
                        UUID.randomUUID() + "_" + "Test1.jpg",
                        UUID.randomUUID() + "_" + "Test2.jpg"));

        productService.register(productDTO);
    }
}
