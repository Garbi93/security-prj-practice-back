package com.example.securityprjpracticeback.controller;

import com.example.securityprjpracticeback.util.CustomJWTException;
import com.example.securityprjpracticeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class APIRefreshController {

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
                String refreshToken
    ) {
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID STRING");
        }

        // Bearer 잘라내서 받기
        String accessToken = authHeader.substring(7);

        // accessToken 의 만료 여부 확인
        // accessToken 이 만료 되지 않았다면
        if (checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // accessToken 이 만료 되었든 아니든 여기로 넘어옴
        // refreshToken 검사 해보기
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh .... claims: " + claims);

        // 새로운 accessToken 발급 받기
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        // 만약 refreshToken 이 1시간 미만인 상태라면 ? -> refreshToken 을 새로 생성 해주기 : 아니면 기존것 그대로 사용
        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        // 새로운 accessToken 과 refreshToken 을 전달 하기
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    //시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp) {
        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long) exp * (1000));
        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();
        //분단위 계산
        long leftMin = gap / (1000 * 60);
        //1시간도 안남았는지..
        return leftMin < 60;
    }

    // accessToken 을 받아와 만료 여부 확인후 true or false 를 전달
    private boolean checkExpiredToken(String token) {
        try{
            JWTUtil.validateToken(token);
        }catch(CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }
}
