package com.pluralsight;

import com.pluralsight.repository.HibernateSpeakerRepositoryImpl;
import com.pluralsight.repository.SpeakerRepository;
import com.pluralsight.service.SpeakerService;
import com.pluralsight.service.SpeakerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
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
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }
}
