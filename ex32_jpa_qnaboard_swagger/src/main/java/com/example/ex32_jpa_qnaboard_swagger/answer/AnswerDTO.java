package com.example.ex32_jpa_qnaboard_swagger.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {

    @NotEmpty(message = "답변 내용은 필수 항목입니다.")
    private String content;

}