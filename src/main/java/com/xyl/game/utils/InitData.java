package com.xyl.game.utils;

import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.po.AnnualMeetingGameQuestion;
import com.xyl.game.vo.AnnualMeetingGameQuestionVo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * initData
 *
 * @author Naah
 * @date 2018-01-22
 */
public class InitData {
    private final static Logger logger = Logger.getLogger(InitData.class);

    /**
     * 初始化数据
     *
     * @param mapper  数据库mapper
     * @param context spring容器
     */
    public static void initData(AnnualMeetingGameQuestionMapper mapper, ApplicationContext context) {
        initLog4j();
        HeapVariable.mapper = mapper;
        logger.info("初始化数据库mapper");
        HeapVariable.context = context;
        logger.info("初始化Spring容器");
        initVariable();
        initQuestion();
    }

    /**
     * 初始化Log4j
     */
    public static void initLog4j() {
        PropertyConfigurator.configure(PropertiesUtils.initProperties("log4j.properties"));
        logger.info("初始化Log4j");
    }


    /**
     * 初始化变量
     */
    public static void initVariable() {
        HeapVariable.beginTime = null;
        logger.info("初始化游戏开始时间");
        HeapVariable.now = null;
        logger.info("初始化当前题目");
        HeapVariable.usersMap = new ConcurrentHashMap<>(1024);
        logger.info("初始化用户数据");
        HeapVariable.annualMeetingGameQuestionVos = new HashMap<String, AnnualMeetingGameQuestionVo>(64);
    }

    /**
     * 初始化问题
     */
    public static void initQuestion() {
        AnnualMeetingGameQuestionMapper mapper = HeapVariable.mapper;
        List<AnnualMeetingGameQuestion> questionList = mapper.selectAll();
        List<QuestionDTO> questionDTOList = new ArrayList<>(questionList.size());
        for (AnnualMeetingGameQuestion question : questionList) {
            questionDTOList.add(new QuestionDTO(question.getId(), question.getTopic(), question.getAnswerOne(),
                    question.getAnswerTwo(), question.getAnswerThree(), question.getAnswerFour()));
        }
        HeapVariable.questionsList = questionList;
        logger.info("初始化题目");
        HeapVariable.questionDTOList = questionDTOList;
        logger.info("初始化题目DTO");
        initCount();

    }

    /**
     * 初始化统计数据
     */
    public static void initCount() {
        List<AnnualMeetingGameQuestion> questionList = HeapVariable.questionsList;
        HeapVariable.atomic = new AtomicInteger();
        HeapVariable.atomic.set(questionList.size());
        ;
        HeapVariable.answerList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < HeapVariable.questionsList.size(); i++) {
            ConcurrentHashMap<Byte, AtomicInteger> hashMap = new ConcurrentHashMap<>(5);
            HeapVariable.answerList.add(hashMap);
            hashMap.put((byte) 0, new AtomicInteger());
            hashMap.put((byte) 1, new AtomicInteger());
            hashMap.put((byte) 2, new AtomicInteger());
            hashMap.put((byte) 3, new AtomicInteger());
            hashMap.put((byte) 4, new AtomicInteger());
        }
        logger.info("初始化答案统计数据");
        HeapVariable.isSendAnswer = new AtomicBoolean();
        HeapVariable.isSendAnswer.set(true);
        logger.info("初始化是否发送答案");

    }

    /**
     * 清空数据库表
     */
    public static void initTable() {
        AnnualMeetingGameQuestionMapper mapper = HeapVariable.mapper;
        mapper.copyTableStruct();
        mapper.deleteTable();
        mapper.renameTableName();
        logger.info("清空数据库表");
    }

    /**
     * 初始化游戏
     */
    public static void initGame() {
        initVariable();
        initCount();
        ExcelUtil.savaUserData("clearUserData");
    }
}
