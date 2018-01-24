package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.Admin;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.User;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
import org.springframework.context.ApplicationContext;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * HeapVariable
 *
 * 内存变量
 * @author Naah
 * @date 2018-01-21
 */


public class HeapVariable {
    public static List<AnnualMeetingGameQuestion> questionsList;
    public static List<QuestionDTO> questionDTOList;
    public static Map<String,User> usersMap;
    public static Map<String,AnnualMeetingGameQuestionVo> annualMeetingGameQuestionVos;
    public static ApplicationContext context;
    public static AnnualMeetingGameQuestionMapper mapper;
    public static AtomicInteger atomic;
    public static String MD5DataChange = "";
    public static Timestamp beginTime;
    public static QuestionDTO now;
    public static int intervalSecond=12;
    public static CopyOnWriteArrayList<Map<Byte,AtomicInteger>> answerList;
    public static Admin pwd;
    public static AtomicBoolean isSendAnswer;

}
