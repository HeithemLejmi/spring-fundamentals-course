package com.pluralsight.service;

import com.pluralsight.model.Speaker;
import com.pluralsight.repository.SpeakerRepository;
import java.util.List;
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

}
