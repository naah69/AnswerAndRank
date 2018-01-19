package com.xyl.game;

import com.xyl.game.utils.PropertiesUtils;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBOOT 启动类
 * @author Naah
 */

@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {
		PropertyConfigurator.configure(PropertiesUtils.initProperties("log4j.properties"));
		SpringApplication.run(GameApplication.class, args);
	}
}
