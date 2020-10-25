package com.pluralsight;

import com.pluralsight.repository.HibernateSpeakerRepositoryImpl;
import com.pluralsight.repository.SpeakerRepository;
import com.pluralsight.service.SpeakerService;
import com.pluralsight.service.SpeakerServiceImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  @Scope(value = BeanDefinition.SCOPE_SINGLETON)
  public SpeakerService getSpeakerService(){
    // using Constructor injection to inject the collaborating bean "speakerRepository" inside the "speakerService":
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl(getSpeakerRepository());

    /** Using Setter Injection of the collaborating bean "speakerRepository" inside the "speakerService":
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl();
    speakerService.setSpeakerRepository(getSpeakerRepository());
     */

    return speakerService;
  }

  @Bean(name = "speakerRepository")
  @Scope(value = BeanDefinition.SCOPE_SINGLETON)
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }
}
