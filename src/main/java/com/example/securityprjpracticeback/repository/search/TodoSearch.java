package com.example.securityprjpracticeback.repository.search;

import com.example.securityprjpracticeback.domain.Todo;
import org.springframework.data.domain.Page;

public interface TodoSearch {
    Page<Todo> search1();
}
