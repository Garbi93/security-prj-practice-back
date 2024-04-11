package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.dto.TodoDTO;
import jakarta.transaction.Transactional;

@Transactional
public interface TodoService {

    TodoDTO get(Long tno);

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
