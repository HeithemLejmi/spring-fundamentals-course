package com.pluralsight;

import com.pluralsight.util.CalendarFactory;
import java.util.Calendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AppConfig {

  @Bean(name = "cal")
  public CalendarFactory calFactory(){
    CalendarFactory calendarFactory = new CalendarFactory();
    calendarFactory.addDays(2);
    return calendarFactory;
  }

  @Bean
  Calendar cal() throws Exception{
    return calFactory().getObject();
  }
}
