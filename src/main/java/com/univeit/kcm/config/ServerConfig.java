package com.univeit.kcm.config;

import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ServerConfig {
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		
		tomcat.addAdditionalTomcatConnectors(getHttpConnector());
		
		return tomcat;
	}
	
	private Connector getHttpConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8098);
		connector.setSecure(false);
		connector.setRedirectPort(8099);
		return connector;
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("mail.univeit.com");
	    mailSender.setPort(465);
	     
	    mailSender.setUsername("accounts.kcm@univeit.com");
	    mailSender.setPassword("!q,@$Cr7I;EO");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    //props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    //props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.ssl.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
