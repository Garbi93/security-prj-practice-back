package com.example.securityprjpracticeback.repository;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoSearch {

}
