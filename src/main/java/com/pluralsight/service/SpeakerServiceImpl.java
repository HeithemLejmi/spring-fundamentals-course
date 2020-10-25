package com.pluralsight.service;

import com.pluralsight.model.Speaker;
import com.pluralsight.repository.SpeakerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class SpeakerServiceImpl implements SpeakerService {

  SpeakerRepository repository;

  public SpeakerServiceImpl(){
    System.out.println("SpeakerService NoArgsConstructor");
  }

  public SpeakerServiceImpl(SpeakerRepository speakerRepository){
    System.out.println("SpeakerService AllArgsConstructor");
    this.repository = speakerRepository;
  }

  @Autowired
  public void setSpeakerRepository(SpeakerRepository speakerRepository){
    System.out.println("SpeakerService Setter");
    this.repository = speakerRepository;
  }

  public List<Speaker> findAll(){
    return repository.findAll();
  }

}
