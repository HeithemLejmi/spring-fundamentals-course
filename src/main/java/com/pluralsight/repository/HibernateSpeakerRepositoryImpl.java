package com.pluralsight.repository;

import com.pluralsight.model.Speaker;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("speakerRepository")
public class HibernateSpeakerRepositoryImpl implements SpeakerRepository {


  public List<Speaker> findAll(){
    List<Speaker> speakers = new ArrayList<Speaker>();
    Speaker speaker = new Speaker();
    speaker.setFirstName("Heithem");
    speaker.setLastName("Lejmi");
    speakers.add(speaker);
    return speakers;
  }

}
