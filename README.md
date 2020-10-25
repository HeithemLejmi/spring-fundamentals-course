# spring-fundamentals-course

## 2. Architecture and Project Setup [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/3_architecture-and-project-setup-slides.pdf)

## 3. Spring Configuration Using Java [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/4_spring-configuration-using-java-slides.pdf)

### 3.1. @Configuration
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
### 3.2. @Bean
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

### 3.3. Setter Injection : 
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

### 3.4. Constructor Injection:
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
### 4.1. Scopes: 
There are 5 scopes available inside of Spring for us to configure a bean inside of our application. 
- Valid in any configuration:
  - Singleton Scope: is the default scope.
  - Prototype Scope: which is actually a **new bean per request**
- Scopes valid only in web-aware Spring projects (*) are : 
  - request
  - session
  - global
(*) So if you're implementing Spring MVC or even doing a single-page model application to where you're doing microservices
 to your front end, those 3 scopes are only available in that context. 

##### Singleton Scope:
- The singleton design pattern restricts the instantiation of a class to just one object. 
- A singleton is the default bean scope inside of Spring. 
So if you don't give it a scope, it will automatically be assigned the default scope of singleton, which means that **there
 is one instance per Spring container or application context**.
- To apply the Singleton Scope on a Bean, we only need to annotate that Bean with **@Scope** annotation and select the
 **value** : *BeanDefinition.SCOPE_SINGLETON*.
```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  @Scope(value = BeanDefinition.SCOPE_SINGLETON)
  public SpeakerService getSpeakerService(){
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl(getSpeakerRepository());
    return speakerService;
  }
}
```
In order to test whether we have one and unique object of a bean we can create two instances of the same bean and check
 if they point to the same object or not:
```java
   public static void main(String args[]){
     // Create the application context based on the @Configuration class : which creates the Spring IoC Container
     ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
     // We are creating two instances of the same bean "speakerService":
     SpeakerService speakerService1 = context.getBean("speakerService", SpeakerService.class);
     SpeakerService speakerService2 = context.getBean("speakerService", SpeakerService.class);
     // Here we can test that both instances are pointing to the same object:
     SpeakerService speakerService2 = context.getBean("speakerService", SpeakerService.class);
    System.out.println("Address of the 1st instance is : " + speakerService1);
    System.out.println("Address of the 2nd instance is : " + speakerService2);
   }
```
The output of these `System.out.println` is the same address => same object => `SINGLETON_PATTERN` proven:
```
Address of the 1st instance is : com.pluralsight.service.SpeakerServiceImpl@581ac8a8
Address of the 2nd instance is : com.pluralsight.service.SpeakerServiceImpl@581ac8a8
```
##### Prototype Scope:
- The prototype design pattern guarantees **a unique instance per request**, and, thus, the scope inside of a Spring
 container mimics that design pattern: **Each time you request a bean from the container, you're guaranteed a unique
  instance.** 
- It is essentially the opposite of a singleton. 
- In order to apply the **Prototype Scope, we need to replace the **value** inside the **@Scope** annotation by
 SCOPE_PROTOTYPE instead of SCOPE_SINGLETON. 
 ```java
 @Configuration
 public class AppConfig {
 
   @Bean(name = "speakerService")
   @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
   public SpeakerService getSpeakerService(){
     SpeakerServiceImpl speakerService = new SpeakerServiceImpl(getSpeakerRepository());
     return speakerService;
   }
 }
 ```
In order to test whether we have one and unique object of a bean we can create two instances of the same bean and check
 if they point to the same object or not:
```java
   public static void main(String args[]){
     // Create the application context based on the @Configuration class : which creates the Spring IoC Container
     ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
     // We are creating two instances of the same bean "speakerService":
     SpeakerService speakerService1 = context.getBean("speakerService", SpeakerService.class);
     SpeakerService speakerService2 = context.getBean("speakerService", SpeakerService.class);
     // Here we can test that both instances are pointing to the same object:
     SpeakerService speakerService2 = context.getBean("speakerService", SpeakerService.class);
    System.out.println("Address of the 1st instance is : " + speakerService1);
    System.out.println("Address of the 2nd instance is : " + speakerService2);
   }
```
The output of these `System.out.println` is 2 different addresses => so we really have one instance per request (our object address has changed per request)
 => `PROTOTYPE_PATTERN` proven:
```
Address of the 1st instance is : com.pluralsight.service.SpeakerServiceImpl@78123e82
Address of the 2nd instance is : com.pluralsight.service.SpeakerServiceImpl@67c33749
```
NB: it is now giving us **a unique bean per request**, now, per request of the bean from the context. 
If you hang onto that bean and do stuff with it for the next 10 minutes, it will still be the same bean, but every time we ask for a new one for the container, it's going to give us a unique one back. 

Just so we don't mess up any future demos, let's go ahead and change that back to SINGLETON. (for the rest of the course)

#### Web Scopes:
Web scopes are beyond this course since it's just a basic Spring fundamentals course. 
They are covered more in the Introduction to Spring MVC course that's available here on Pluralsight.

There are 3  web scopes: 
- Request scope: 
  - **which returns a bean per HTTP request**, 
  - which sounds a lot like prototype except it's for the lifecycle of a bean request, which is fairly short, 
  but longer than prototype where it's one instance per every time I ask the container for a bean. 
- Session scope: 
  - just **returns a single bean per HTTP session**, and that will live as long as that user session is alive, so 10
 minutes, 20 minutes, 30 minutes, however long they're alive on that website a bean of scope session will stick around. 
- globalSession Scope:
  - which **will return a single bean per application**, 
  - so once I access it, it's alive for the duration of that application, not just my visit to that application. 
  - You could think of it as singleton, but it's really the entire life of that application on the server until it gets
   undeployed or the server gets rebooted.
 
### 4.2. Autowired:
Autowiring is a great technique used to reduce the wiring up and configuration of code. 
If you've ever heard the term convention over configuration, this is it. (reduce the configuration code in the
 @Configuration class thanks to the @Autowired annotation)
To autowire our applications using the Java configuration:
- we just simply need to add a @ComponentScan to our @Configuration class, to scan the package `com.pluralsight` for Beans, this is where I should begin looking for autowired annotations. 
- To use autowiring, you just mark whatever bean you want as autowired (to inject this Bean as a field, or an argument
 for a setter/constructor): 
  - you can choose by name, and that uses the @Bean name
  - or by type, and that uses the instance type. 
- For example, we marked the setter in the `SpeakerServiceImpl` with `@Autowired` in order to wire the 
`speakerRepository` collaborating bean to the `speakerService` bean:
  
  ```java 
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
  ```
- And in the `@Configuration` class we only call the NoArgsConstructor of the `speakerService`, and re-run the application: 
 ```java
@Configuration
public class AppConfig {

  @Bean(name = "speakerService")
  @Scope(value = BeanDefinition.SCOPE_SINGLETON)
  public SpeakerService getSpeakerService(){
    SpeakerServiceImpl speakerService = new SpeakerServiceImpl();
    return speakerService;
  }

  @Bean(name = "speakerRepository")
  @Scope(value = BeanDefinition.SCOPE_SINGLETON)
  public SpeakerRepository getSpeakerRepository(){
    return new HibernateSpeakerRepositoryImpl();
  }
}
 ```
The output on the terminal is as below, which means that the wiring was successful, and that we go through the setter:
 ```
SpeakerService NoArgsConstructor
SpeakerService Setter
Heithem
 ```
## 5. Spring Configuration Using XML [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/6_spring-configuration-using-xml-slides.pdf)

## 6. Advanced Bean Configuration [here](https://github.com/HeithemLejmi/spring-fundamentals-course/blob/doc/add_documentation/doc/7_advanced-bean-configuration-slides.pdf)