package com.xyl.bulletchat;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@SpringBootApplication
public class WebsockerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsockerApplication.class, args);
         PropertyConfigurator.configure(initProperties("log4j.properties"));
    }

     /**
     * 初始化配置文件
     * @param fileName
     * @return
     */
	public static Properties initProperties(String fileName) {
		Properties properties = new Properties();
		InputStreamReader input = null;
		try {
		    // 加载Java项目根路径下的配置文件
			input = new InputStreamReader(WebsockerApplication.class.getResourceAsStream("/" + fileName), "UTF-8");
			// 加载属性文件
			properties.load(input);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
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
