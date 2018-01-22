package com.xyl.game.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ntp.TimeStamp;

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
    private Byte answer;
    private Integer time;
    private Boolean isRight;
    private TimeStamp commitTime;
}
