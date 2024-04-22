package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.domain.Product;
import com.example.securityprjpracticeback.domain.ProductImage;
import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.ProductDTO;
import com.example.securityprjpracticeback.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());


        Page<Object[]> result = productRepository.selectList(pageable);

        // Object[] => 0 product 1 productImage 형식으로 쌓여있다.

        List<ProductDTO> dtoList = result.get().map(arr -> {
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));


            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
