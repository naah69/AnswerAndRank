package com.xyl.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * QuestionDTO
 *
 * @author Naah
 * @date 2018-01-21
 */
@AllArgsConstructor
@Getter
public class QuestionDTO {
    private  Integer id;
    private String question;

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }
}
