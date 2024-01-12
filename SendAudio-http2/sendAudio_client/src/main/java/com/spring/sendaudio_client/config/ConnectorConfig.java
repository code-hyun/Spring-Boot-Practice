package com.spring.sendaudio_client.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration
 *  http 연결 요청 받을 커넥터 설정
 * */
@Configuration
public class ConnectorConfig {
    @Bean
    public ServletWebServerFactory serverFactory(){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 커넥터 추가
        return tomcat;
    }

    private Connector createStandardConnector(){ // 커넥터 리턴 메소드
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8080); // https가 적용 되지 않는 요청에 대한 포트
        return connector;
    }
}
