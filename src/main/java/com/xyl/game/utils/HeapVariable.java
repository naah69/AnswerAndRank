package com.xyl.game.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.context.ApplicationContext;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.po.User;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
<<<<<<< HEAD
=======
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
>>>>>>> branch 'master' of http://192.168.79.124:8080/nayan3480232/annual-meeting-answer-game-match.git

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
<<<<<<< HEAD
    public static String MD5DataChange = "";
    public static TimeStamp beginTime;
    public static QuestionDTO now;
    public static ConcurrentLinkedQueue<QuestionDTO> questionQueue;
    
    
=======
    public static TimeStamp beginTime;
    public static QuestionDTO now;
    public static ConcurrentLinkedQueue<QuestionDTO> questionQueue;
>>>>>>> branch 'master' of http://192.168.79.124:8080/nayan3480232/annual-meeting-answer-game-match.git
}
