package ru.ptkom.provisor.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {


    @Bean
    public org.slf4j.Logger logger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();


        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d : %p : %m%n");
        //encoder.setPattern("%d %p %c{1.} [%t] %m%n");
        encoder.start();


        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("FILE");
        fileAppender.setFile("/var/log/provisor.log");


        SizeAndTimeBasedRollingPolicy rollingPolicy = new SizeAndTimeBasedRollingPolicy();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setParent(fileAppender);
        rollingPolicy.setFileNamePattern("/var/log/provisor.%d{yyyy-MM-dd}.%i.log");
        rollingPolicy.setMaxFileSize(FileSize.valueOf("10MB"));
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.start();


        fileAppender.setRollingPolicy(rollingPolicy);
        fileAppender.setEncoder(encoder);
        fileAppender.start();


        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName("CONSOLE");
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();


        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(fileAppender);
        rootLogger.addAppender(consoleAppender);


        return LoggerFactory.getLogger("provisor");
    }
}