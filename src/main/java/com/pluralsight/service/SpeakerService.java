package com.pluralsight.service;

import com.pluralsight.model.Speaker;
import com.pluralsight.repository.SpeakerRepository;
import java.util.List;

public interface SpeakerService {

  void setSpeakerRepository(SpeakerRepository repository);
  List<Speaker> findAll();
}
