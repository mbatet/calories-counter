package org.mbatet.calories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@SpringBootApplication
@Configuration
public class CaloriesCounterApplication extends SpringBootServletInitializer implements CommandLineRunner  {

	@Value(value = "${app.name}")
	String appName;
	@Value(value = "${server.name}")
	String serverName;
	@Value(value = "${server.servlet.context-path}")
	String contextPath;


	@Autowired
	private ApplicationContext context;

	private static final Logger logger = LoggerFactory.getLogger(CaloriesCounterApplication.class);

	@Override
	public void run(String... arg0) throws Exception {

		String env = Arrays.toString(context.getEnvironment().getActiveProfiles());
		logger.info("****************************************************************************************");
		logger.info("************************ " + appName + " / " + env + " ****************************");
		logger.info( "********************* " + serverName + contextPath + " ***********************" );
		logger.info("****************************************************************************************");

	}

	public static void main(String[] args) {
		SpringApplication.run(CaloriesCounterApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CaloriesCounterApplication.class);
	}

	@PreDestroy
	public void onShutDown() {

		logger.info("****************************************************************************************");
		logger.info("*******************************Shutting down....****************************************");
		logger.info("****************************************************************************************");

		ConfigurableApplicationContext a = ( ConfigurableApplicationContext) context;

		a.stop();

	}
}
