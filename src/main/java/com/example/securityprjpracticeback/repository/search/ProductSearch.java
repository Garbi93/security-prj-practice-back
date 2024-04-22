package com.example.securityprjpracticeback.repository.search;

import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.ProductDTO;

public interface ProductSearch {
    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);
}
