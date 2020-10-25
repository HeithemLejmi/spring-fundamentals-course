package com.pluralsight;

import com.pluralsight.service.SpeakerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
  public static void main(String args[]){
    // Create the application context based on the @Configuration class : which creates the Spring IoC Container
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    // The Bean "speakerService" will retrieve its dependency "speakerRepository" from this container
    SpeakerService speakerService = context.getBean("speakerService", SpeakerService.class);
    // Here we can test that we really get the dependency injected inside "speakerService"
    System.out.println(speakerService.findAll().get(0).getFirstName());
    System.out.println(speakerService.findAll().get(0).getAge());
  }



}
