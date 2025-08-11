package com.example.wippo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data                   // Getter, Setter, toString 자동 생성
@AllArgsConstructor    // 모든 필드를 받는 생성자 자동 생성
public class TestResponse {
    private String message;
    private int count;
}
