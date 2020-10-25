package com.pluralsight.repository;

import com.pluralsight.model.Speaker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("speakerRepository")
public class HibernateSpeakerRepositoryImpl implements SpeakerRepository {

  @Autowired
  Calendar cal;

  public List<Speaker> findAll(){
    List<Speaker> speakers = new ArrayList<Speaker>();
    Speaker speaker = new Speaker();
    speaker.setFirstName("Heithem");
    speaker.setLastName("Lejmi");
    speakers.add(speaker);
    System.out.println("Cal is: " + cal.getTime());
    return speakers;
  }

}
