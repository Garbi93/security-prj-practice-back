package com.example.securityprjpracticeback.controller;

import com.example.securityprjpracticeback.dto.PageRequestDTO;
import com.example.securityprjpracticeback.dto.PageResponseDTO;
import com.example.securityprjpracticeback.dto.TodoDTO;
import com.example.securityprjpracticeback.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    // 한개의 투두 조회
    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    // 투두 리스트 목록 조회
    @GetMapping("list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list..........." + pageRequestDTO);

        return todoService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO dto) {
        log.info("todoDTO: " + dto);

        Long tno = todoService.register(dto);

        return Map.of("TNO", tno);

    }

    // 투두 수정
    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno,
                                      @RequestBody TodoDTO todoDTO) {
        todoDTO.setTno(tno);

        todoService.modify(todoDTO);

        return Map.of("RESULT", "SUCCESS");
    }

    // 투두 삭제
    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable Long tno) {

        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
    }

}
