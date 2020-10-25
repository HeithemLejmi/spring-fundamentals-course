# spring-fundamentals-course

## 2. Architecture and Project Setup [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/3_architecture-and-project-setup-slides.pdf)

## 3. Spring Configuration Using Java [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/4_spring-configuration-using-java-slides.pdf)

### a. @Configuration
- To start the configuration of our application, we're going to use a @Configuration annotation. 
- The Java classes that have the configuration annotation replace any XML files that we could've used historically:

--> The AppConfig class, in this project, (annotated with @Configuration will replace the applicationContext.xml)
**NB: the applicationContext.xml is replaced by the @Configuration** 
- @Configuration is a class-level annotation.
- In that AppConfig class, that we just created in our demo, we now put a @Configuration annotation 
at the top of that class (at the class level) and say @Configuration. 
=> Now this signifies that this file is to be used for configuration purposes. 
```java
@Configuration
public class AppConfig {

}
```
### b. @Bean
- @Bean annotation is a Method level annotation: used to define/construct Spring Bean, inside a class annotated with
 @Configuration.
- this annotation is used to get instances of Spring beans : a method annotated with @Bean returns an instance of Spring bean, 
and that the bean is now registered inside of Spring and available for us to use inside of our Spring application. 
- To add a bean, inside a Spring component:
 1. we can start off by just creating a public method. This method **can't be void**, it should return an instance of a
  Service, Repository, etc ...
 2. And we need to annotate this method as a bean: @Bean. We can optionally, we don't have to, optionally give this a name. 
The name of the Bean should be in lower camel case, just like we would name a variable. 
```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  public SpeakerService getSpeakerService(){
    return new SpeakerServiceImpl();
  }
}
```

This is it. We've literally: 
1. Spring enabled our application. 
2. We've told it to configure this as a Spring app, and we've created a bean, (in the example : our speakerService). 

### c. Setter Injection : 
Setter injection using the Java configuration approach is simply a matter of **calling a setter on a bean**. 
It is used to inject a collaborating bean, inside another bean:
We know already that SpeakerService has a dependency to SpeakerRepository => So we are going to inject the dependency
 "speakerRepository" (the collaborating bean) into the speakerService bean, using **Setter Injection**:
- **Step 1:** create the collaborating bean: speakerRepository, using the bean annotation like we did in the previous demo
(we define a method called **getSpeakerRepository** that define/return a bean of type SpeakerRepository and named
 speakerRepository )
```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  public SpeakerService getSpeakerService(){
    return new SpeakerServiceImpl();
  }

  @Bean(name = "speakerRepository")
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }

}
```
- **Step 2:** Define a setter method inside the principal bean, (in this case SpeakerService and SpeakerServiceImpl), in order to inject its
 dependency (speakerRepository) in it: 
```java
public class SpeakerServiceImpl implements SpeakerService {

  SpeakerRepository repository;

  public void setSpeakerRepository(SpeakerRepository speakerRepository){
    this.repository = speakerRepository;
  }

  public List<Speaker> findAll(){
    return repository.findAll();
  }

}
```
- **Step 3 :** Then we call this setter inside the method creating the SpeakerService bean, in order to inject the
instance of speakerRepository inside the bean SpeakerService (at the moment of the creation of this SpeakerService bean). 
We call the setter method with the argument : **getSpeakerRepository** (that's registered as a Spring bean, and that's
 going to return an instance of speakerRepository). 
 
**This step  is going to wire the SpeakerRepository inside of our SpeakerService.** :
```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  public SpeakerService getSpeakerService(){
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl();
    // Setter Injection of the collaborating bean "speakerRepository" inside the "speakerService":
    speakerService.setSpeakerRepository(getSpeakerRepository());
    return speakerService;
  }

  @Bean(name = "speakerRepository")
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }

}
```
- **Step 4 :** Last but not least, inside the main method of our Application, we should create the **ApplicationContext** using this
 configuration class (AppConfig), which will create the **Spring IoC Container**. And from this container, we are going to
  retrieve the beans that are registered there:
```java
  public static void main(String args[]){
    // Create the application context based on the @Configuration class : which creates the Spring IoC Container
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    // The Bean "speakerService" will retrieve its dependency "speakerRepository" from this container
    SpeakerService speakerService = context.getBean("speakerService", SpeakerService.class);

    // Here we can test that we really get the dependency injected inside "speakerService"
    System.out.println(speakerService.findAll().get(0).getFirstName());
  }
```

### d. Constructor Injection:
Constructor injection is just like setter injection. 
Instead of calling the setter, we call the defined constructor of the **SpeakerServiceImpl** and we pass the collaborating
 bean **speakerRepository** as an argument to this constructor and that is it.
- **Step 1**: Define the constructor that takes **speakerRepository** as an argument:
```java
public class SpeakerServiceImpl implements SpeakerService {

  SpeakerRepository repository;

  /** Constructor **/
  public SpeakerServiceImpl(SpeakerRepository speakerRepository){
    this.repository = speakerRepository;
  }

  public List<Speaker> findAll(){
    return repository.findAll();
  }

}
```

- **Step 2**: Replace the setter with the constructor, and inject the collaborating bean **speakerRepository** as an
 argument of this constructor:
```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  public SpeakerService getSpeakerService(){
    // using Constructor injection to inject the collaborating bean "speakerRepository" inside the "speakerService":
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl(getSpeakerRepository());
    return speakerService;
  }

  @Bean(name = "speakerRepository")
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }
}
```

## 4. Spring Scopes and Autowiring [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/5_spring-scopes-and-autowiring-slides.pdf)

## 5. Spring Configuration Using XML [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/6_spring-configuration-using-xml-slides.pdf)

## 6. Advanced Bean Configuration [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/7_advanced-bean-configuration-slides.pdf)