package com.example.securityprjpracticeback.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {

    private List<E> dtoList;

    private List<Integer> pageNumberList;

    private PageRequestDTO pageRequestDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long total) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int)total;

        // 끝 페이지 계산 end
        int end = (int)(Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 시작 페이지 계산 start
        int start = end - 9;

        // 진짜 마지막 페이지 계산 last
        int last = (int)(Math.ceil(totalCount / (double)pageRequestDTO.getSize()));

        // 끝 페이지가 last 보다 클때는 last 로 작을때는 end 로 끝 페이지 설정
        end = end > last ? last : end;

        // 이전 목록 버튼 -> 1 보다 클때만 나타나게
        this.prev = start > 1;

        // 다음 목록 버튼 -> 전체 글 목록 갯수 보다 마지막 페이지 * 페이지 사이즈 보다 작을때만 나타나게
        this.next = totalCount > end * pageRequestDTO.getSize();

        // 현재 페이지 번호의 목록
        this.pageNumberList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        // 이전 페이지 번호 -> 이전 페이지 버튼 이 있다면 start - 1 해라
        this.prevPage = prev ? start - 1 : 0;

        // 다음 페이지 번호 -> 다음 페이지 버튼이 있다면 end - 1 해라
        this.nextPage = next ? end + 1 : 0;


    }
}
