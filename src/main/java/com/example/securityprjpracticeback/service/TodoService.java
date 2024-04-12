package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.TodoDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface TodoService {

    // 조회 기능
    TodoDTO get(Long tno);

    // 등록 기능
    Long register(TodoDTO dto);

    // 수정 기능
    void modify(TodoDTO dto);

    // 삭제 기능
    void remove(Long tno);

    // 페이징 기능
    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);


    default TodoDTO entityToDTO(Todo todo) {
        return TodoDTO.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate())
                .build();
    }

    default Todo dtoToEntity(TodoDTO todoDTO) {
        return Todo.builder()
                .tno(todoDTO.getTno())
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .complete(todoDTO.isComplete())
                .dueDate(todoDTO.getDueDate())
                .build();
    }

}
