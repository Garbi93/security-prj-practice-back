package com.example.securityprjpracticeback.repository.search;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<Todo> search1(PageRequestDTO pageRequestDTO);
}
