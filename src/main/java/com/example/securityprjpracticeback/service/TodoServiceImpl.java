package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.dto.TodoDTO;
import com.example.securityprjpracticeback.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;

    // 조회 기능
    @Override
    public TodoDTO get(Long tno) {

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        return entityToDTO(todo);
    }

    // 등록 기능
    @Override
    public Long register(TodoDTO dto) {
        Todo todo = dtoToEntity(dto);

        Todo result = todoRepository.save(todo);

        return result.getTno();
    }

    // 수정 기능
    @Override
    public void modify(TodoDTO dto) {

        // 조회 기능으로 갖고 오기
        Optional<Todo> result = todoRepository.findById(dto.getTno());
        // 조회 한것을 todo 변수에 저장 하기
        Todo todo = result.orElseThrow();

        // todo 변수 수정 하기
        todo.changeTitle(dto.getTitle());
        todo.changeContent(dto.getContent());
        todo.changeComplete(dto.isComplete());
        todo.changeDueDate(dto.getDueDate());

        todoRepository.save(todo);
    }

    // 삭제 기능
    @Override
    public void remove(Long tno) {
        // id 값으로 삭제하기
        todoRepository.deleteById(tno);
    }
}
