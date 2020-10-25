package com.pluralsight.service;

import com.pluralsight.model.Speaker;
import com.pluralsight.repository.SpeakerRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("speakerService")
public class SpeakerServiceImpl implements SpeakerService {
  @Autowired
  @Qualifier(value = "speakerRepository")
  SpeakerRepository repository;

  public SpeakerServiceImpl(){
    System.out.println("SpeakerService NoArgsConstructor");
  }

  public SpeakerServiceImpl(SpeakerRepository speakerRepository){
    System.out.println("SpeakerService AllArgsConstructor");
    this.repository = speakerRepository;
  }


  public void setSpeakerRepository(SpeakerRepository speakerRepository){
    System.out.println("SpeakerService Setter");
    this.repository = speakerRepository;
  }

  public List<Speaker> findAll(){
    return repository.findAll();
  }

  /**
   * Annotating this initMethod with @PostConstruct means that this method is going to be called directly after the
   * Constructors of this Bean (just after the Creation of the Bean, and before any other business logic (before calling
   * the findAll() method)
   */
  @PostConstruct
  private void intialize(){
    System.out.println("This init method should be called directly first, after creating the speakerService bean");
  }

}
