package com.xyl.game;

import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.utils.HeapVariable;
import com.xyl.game.utils.InitData;
import com.xyl.game.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * SpringBOOT 启动类
 *
 * @author Naah
 */

@SpringBootApplication
@MapperScan("com.xyl.game.mapper")
public class GameApplication {
    private static Logger log = null;

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(GameApplication.class, args);
        AnnualMeetingGameQuestionMapper mapper = context.getBean(AnnualMeetingGameQuestionMapper.class);
        initLog4j();
        initData(mapper, context);

    }

    private static void initLog4j() {
        PropertyConfigurator.configure(PropertiesUtils.initProperties("log4j.properties"));
        log = Logger.getLogger(GameApplication.class);

    }

    private static void initData(AnnualMeetingGameQuestionMapper mapper, ApplicationContext context) {
        HeapVariable.mapper = mapper;
        HeapVariable.context = context;
        InitData.initVariable();
        InitData.initQuestion();
    }
}
