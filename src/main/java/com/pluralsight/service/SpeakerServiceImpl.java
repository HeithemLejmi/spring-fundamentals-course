package com.pluralsight.service;

import com.pluralsight.model.Speaker;
import com.pluralsight.repository.SpeakerRepository;
import java.util.List;

public class SpeakerServiceImpl implements SpeakerService {

  SpeakerRepository repository;

  public SpeakerServiceImpl(SpeakerRepository speakerRepository){
    this.repository = speakerRepository;
  }
  public void setSpeakerRepository(SpeakerRepository speakerRepository){
    this.repository = speakerRepository;
  }

  public List<Speaker> findAll(){
    return repository.findAll();
  }

}
