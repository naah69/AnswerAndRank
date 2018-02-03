package com.xyl.game;

import com.xyl.game.mapper.AnnualMeetingGameQuestionMapper;
import com.xyl.game.utils.InitData;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBOOT 启动类
 *
 * @author Naah
 */

@SpringBootApplication
@MapperScan("com.xyl.game.mapper")
@EnableScheduling
public class GameApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(GameApplication.class, args);
        AnnualMeetingGameQuestionMapper mapper = context.getBean(AnnualMeetingGameQuestionMapper.class);
        StringRedisTemplate redis = context.getBean(StringRedisTemplate.class);
        InitData.initData(mapper, context,redis);
    }




    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory factory =
                new JettyEmbeddedServletContainerFactory();
        return factory;
    }

    @Profile("jetty")
    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
            JettyServerCustomizer jettyServerCustomizer) {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addServerCustomizers(jettyServerCustomizer);
        return factory;
    }


    @Bean
    public JettyServerCustomizer jettyServerCustomizer() {
        return server -> {
            // Tweak the connection config used by Jetty to handle incoming HTTP
            // connections
            final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
            threadPool.setMaxThreads(1000);
            threadPool.setMinThreads(20);
            threadPool.setIdleTimeout(1000*60*30);
        };
    }
}
