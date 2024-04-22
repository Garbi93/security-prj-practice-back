package com.example.securityprjpracticeback.service;

import com.example.securityprjpracticeback.domain.Todo;
import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.TodoDTO;
import com.example.securityprjpracticeback.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 페이징 기능
    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
        // JPA
        Page<Todo> result = todoRepository.search1(pageRequestDTO);

        // Todo List => TodoDTO List 가 되어야 한다.
        List<TodoDTO> dtoList =  result.get().map(todo -> entityToDTO(todo)).collect(Collectors.toList());

        // 목록 데이터 생성
        PageResponseDTO<TodoDTO> responseDTO =
                PageResponseDTO.<TodoDTO>withAll()
                        .dtoList(dtoList)
                        .pageRequestDTO(pageRequestDTO)
                        .totalCount(result.getTotalElements())
                        .build();

        return responseDTO;
    }
}
