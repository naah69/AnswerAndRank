package com.xyl.game.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Answer
 *
 * @author Naah
 * @date 2018-01-21
 */
@Getter
@Setter
@AllArgsConstructor
public class Answer {

    private Integer index;
    private AnnualMeetingGameQuestion question;
    private String answer;
    private Integer time;
    private Boolean isRight;

}
