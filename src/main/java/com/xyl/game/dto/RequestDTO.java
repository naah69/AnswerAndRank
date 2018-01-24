package com.xyl.game.dto;

import com.xyl.game.po.User;
import lombok.Getter;
import lombok.Setter;

/**
 * RequestDTO
 *
 * @author Naah
 * @date 2018-01-22
 */
@Getter
@Setter
public class RequestDTO {
    private String method;
    private Integer id;
    private Integer answer;
    private Integer times;
    private User user;
}
