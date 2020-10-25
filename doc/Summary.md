## What is a Spring Bean? [link](https://www.baeldung.com/spring-bean)

### 1. Bean Definition

Here's a definition of beans in the Spring Framework documentation:

In Spring, the objects that form the backbone of your application and that are managed by the Spring IoC container are called beans. A bean is an object that is instantiated, assembled, and otherwise managed by a Spring IoC container.

This definition is concise and gets to the point, but misses an important thing – Spring IoC container. Let's go down the rabbit hole to see what it is and the benefits it brings in.

### 2. Inversion of Control

Simply put, Inversion of Control, or IoC for short, is a process in which an object defines its dependencies without creating them. This object delegates the job of constructing such dependencies to an IoC container.

Instead of constructing dependencies by itself, an object can retrieve its dependencies from an IoC container. All we need to do is to provide the container with appropriate configuration metadata.

Let's start with the declaration of a couple of domain classes before diving into IoC.

#### 2.1. Traditional Approach (without the IoC container):
Assume we have a class declaration:

```java
public class Company {
    private Address address;
 
    public Company(Address address) {
        this.address = address;
    }
 
    // getter, setter and other properties
}
```

This class needs a collaborator of type Address:
```java
public class Address {
    private String street;
    private int number;
 
    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }
 
    // getters and setters
}
```
Normally, we create objects with their classes' constructors:
```java
Address address = new Address("High Street", 1000);
Company company = new Company(address);
```
There's nothing wrong with this approach, but wouldn't it be nice to manage the dependencies in a better way?

Imagine an application with dozens or even hundreds of classes. Sometimes we want to share a single instance of a class across the whole application, other times we need a separate object for each use case, and so on.

Managing such a number of objects is nothing short of a nightmare. This is where Inversion of Control comes to the rescue.

Instead of constructing dependencies by itself, an object can retrieve its dependencies from an IoC container. All we need to do is to provide the container with appropriate configuration metadata.

#### 2.2. Beans Configuration and IoC in Action:

- First off, let's decorate the Company class with the @Component annotation:
```java
@Component
public class Company {
    // this body is the same as before
}
```

- Here's a configuration class supplying bean metadata to an IoC container:
```java
@Configuration
@ComponentScan(basePackageClasses = Company.class)
public class Config {
    @Bean
    public Address getAddress() {
        return new Address("High Street", 1000);
    }
}
```
The configuration class produces a bean of type Address. It also carries the @ComponentScan annotation, which instructs the container to looks for beans in the package containing the Company class.

**When a Spring IoC container constructs objects of those types, all the objects are called Spring beans as they are
 managed by the IoC container.**


- Since we defined beans in a configuration class, we'll need an instance of the AnnotationConfigApplicationContext class
 to build up a container:
```java
ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
```
A quick test verifies the existence as well as property values of our beans:
```java
Company company = context.getBean("company", Company.class);
assertEquals("High Street", company.getAddress().getStreet());
assertEquals(1000, company.getAddress().getNumber());
The result proves that the IoC container has created and initialized beans correctly.
```

## Spring Bean Annotations [link1](https://www.baeldung.com/spring-bean-annotations), [link2](https://www.baeldung.com/spring-component-repository-service)
In this section, we'll discuss the most common Spring bean annotations used to define different types of beans.

There're several ways to configure beans in a Spring container: 
- We can declare them using XML configuration. 
- We can declare beans using the @Bean annotation in a configuration class.
- Or we can mark the class with one of the annotations from the org.springframework.stereotype package and leave the rest
 to component scanning.
 
### 1. Component Scanning
Spring can automatically scan a package for beans if component scanning is enabled.

@ComponentScan configures which packages to scan for classes with annotation configuration. 
We can specify the base package names directly with one of the basePackages or value arguments (value is an alias for basePackages):
```java      
  @Configuration
  @ComponentScan(basePackages = "com.baeldung.annotations")
  class VehicleFactoryConfig {}
```

Also, we can point to classes in the base packages with the basePackageClasses argument:
```java      
  @Configuration
  @ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
  class VehicleFactoryConfig {}
```

Both arguments are arrays so that we can provide multiple packages for each.
If no argument is specified, the scanning happens from the same package where the @ComponentScan annotated class is present.
@ComponentScan leverages the Java 8 repeating annotations feature, which means we can mark a class with it multiple times:
```     java 
  @Configuration
  @ComponentScan(basePackages = "com.baeldung.annotations")
  @ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
  class VehicleFactoryConfig {}
```
Alternatively, we can use @ComponentScans to specify multiple @ComponentScan configurations:
```java     
   @Configuration
   @ComponentScans({ 
      @ComponentScan(basePackages = "com.baeldung.annotations"), 
      @ComponentScan(basePackageClasses = VehicleFactoryConfig.class)
     })
   class VehicleFactoryConfig {}
```

When using XML configuration, the configuring component scanning is just as easy:
```      
<context:component-scan base-package="com.baeldung" />
```
### 2. @Configuration and @Bean annotations:
@Configuration is a class level annotation, whereas @Bean is a method-level annotation. 
@Configuration classes hold the @ComponentScan annotation.
@Configuration classes can contain bean definition methods annotated with @Bean:
```java
@Configuration
class VehicleFactoryConfig {
 
    @Bean
    Engine engine() {
        return new Engine();
    }
 
}
```
### 3. Stereotype annotations : 
In most typical applications, we have distinct layers like data access, presentation, service, business, etc.
And, in each layer, we have various beans. Simply put, to detect them automatically,  
Spring uses classpath scanning annotations. Then, it registers each bean in the ApplicationContext.
Here's a quick overview of a few of these annotations:
@Component is a generic stereotype for any Spring-managed component
@Service annotates classes at the service layer
@Repository annotates classes at the persistence layer, which will act as a database repository
@Controller serves as a controller in Spring MVC.

- **@Component:**
@Component is a class level annotation. 
During the component scan, Spring Framework automatically detects classes annotated with @Component.

For example:
```java
@Component
class CarUtility {
    // ...
}
```

By default, the bean instances of this class have the same name as the class name with a lowercase initial. 
On top of that, we can specify a different name using the optional value argument of this annotation: @Component(value
="carUtility").

@Service and @Repository are special cases of @Component. They are technically the same but we use them for the different purposes.
And since @Repository, @Service, @Configuration, and @Controller are all meta-annotations of @Component, they share the same
 bean naming behavior. Also, Spring automatically picks them up during the component scanning process.


We can use @Component across the application to mark the beans as Spring's managed components. Spring only pick up and
 registers beans with @Component and doesn't look for @Service and @Repository in general.

They are registered in ApplicationContext because they themselves are annotated with @Component:
```java
@Component
public @interface Service {
}
@Component
public @interface Repository {
}
```

- **@Repository:**
DAO or Repository classes usually represent the database access layer in an application, and should be annotated with @Repository:
```java
@Repository
class VehicleRepository {
    // ...
}
```
One advantage of using this annotation is that it has automatic persistence exception translation enabled. 
When using a persistence framework such as Hibernate, native exceptions thrown within classes annotated with @Repository will be automatically translated into subclasses of Spring's DataAccessExeption.


- **@Service:**
The business logic of an application usually resides within the service layer – so we'll use the @Service annotation to indicate that a class belongs to that layer:
```java
@Service
public class VehicleService {
    // ...    
}
```

- **@Controller:**
@Controller is a class level annotation which tells the Spring Framework that this class serves as a controller in Spring MVC:
```java
@Controller
public class VehicleController {
    // ...
}
```

## Guide to Spring @Autowired [link](https://www.baeldung.com/spring-autowire)
The main annotation of Dependency Injection is @Autowired. 
It allows Spring to resolve and inject collaborating beans into our bean.
In this section, we'll first take a look at how to enable autowiring and the various ways to autowire beans. 
Afterward, we'll talk about resolving bean conflicts using @Qualifier annotation, as well as potential exception scenarios.

### 1. Enabling @Autowired Annotations:
The Spring framework enables automatic dependency injection. In other words, by declaring all the bean dependencies in a Spring configuration file, Spring container can autowire relationships between collaborating beans. This is called Spring bean autowiring.

To use Java-based configuration in our application, let's enable annotation-driven injection to load our Spring configuration:
```java
@Configuration
@ComponentScan("com.baeldung.autowire.sample")
public class AppConfig {}
```
Alternatively, the <context:annotation-config> annotation is mainly used to activate the dependency injection annotations in Spring XML files.

Moreover, Spring Boot introduces the @SpringBootApplication annotation. This single annotation is equivalent to using @Configuration, @EnableAutoConfiguration, and @ComponentScan.

Let's use this annotation in the main class of the application:
```java
@SpringBootApplication
class VehicleFactoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleFactoryApplication.class, args);
    }
}
```
As a result, when we run this Spring Boot application, it will automatically scan the components in the current package and its sub-packages. Thus it will register them in Spring's Application Context, and allow us to inject beans using @Autowired.

### 2. Using @Autowired:
After enabling annotation injection, we can use autowiring on properties, setters, and constructors.

#### 2.1. @Autowired on Properties
Let’s see how we can annotate a property using @Autowired. This eliminates the need for getters and setters.

First, let's define a fooFormatter bean:
```java
@Component("fooFormatter")
public class FooFormatter {
    public String format() {
        return "foo";
    }
}
```

Then, we'll inject this bean into the FooService bean using @Autowired on the field definition:
```java
@Component
public class FooService {  
    @Autowired
    private FooFormatter fooFormatter;
}
```
As a result, Spring injects fooFormatter when FooService is created.

#### 2.2. @Autowired on Setters
Now let's try adding @Autowired annotation on a setter method.

In the following example, the setter method is called with the instance of FooFormatter when FooService is created:
```java
public class FooService {
    private FooFormatter fooFormatter;
    @Autowired
    public void setFooFormatter(FooFormatter fooFormatter) {
        this.fooFormatter = fooFormatter;
    }
}
```
#### 2.3. @Autowired on Constructors:

Finally, let's use @Autowired on a constructor.

We'll see that an instance of FooFormatter is injected by Spring as an argument to the FooService constructor:
```java
public class FooService {
    private FooFormatter fooFormatter;
    @Autowired
    public FooService(FooFormatter fooFormatter) {
        this.fooFormatter = fooFormatter;
    }
}
```
### 3. @Autowired and Optional Dependencies
When a bean is being constructed, the @Autowired dependencies should be available. Otherwise, if Spring cannot resolve a bean for wiring, it will throw an exception.

Consequently, it prevents the Spring container from launching successfully with an exception of the form:
```
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: 
No qualifying bean of type [com.autowire.sample.FooDAO] found for dependency: 
expected at least 1 bean which qualifies as autowire candidate for this dependency. 
Dependency annotations: 
{@org.springframework.beans.factory.annotation.Autowired(required=true)}
```
To fix this, we need to declare a bean of the required type:
```java
public class FooService {
    @Autowired(required = false)
    private FooDAO dataAccessor; 
}
```
### 4. Autowire Disambiguation:
By default, Spring resolves @Autowired entries by type. 
If more than one bean of the same type is available in the container, the framework will throw a fatal exception.

To resolve this conflict, we need to tell Spring explicitly which bean we want to inject.
#### 4.1. Autowiring by @Qualifier:
For instance, let's see how we can use the @Qualifier annotation to indicate the required bean.

First, we'll define 2 beans of type Formatter:
```java
@Component("fooFormatter")
public class FooFormatter implements Formatter {
    public String format() {
        return "foo";
    }
}
@Component("barFormatter")
public class BarFormatter implements Formatter {
    public String format() {
        return "bar";
    }
}
```
Now let's try to inject a Formatter bean into the FooService class:
```java
public class FooService {
    @Autowired
    private Formatter formatter;
}
```
In our example, there are two concrete implementations of Formatter available for the Spring container. 
As a result, Spring will throw a NoUniqueBeanDefinitionException exception when constructing the FooService:
```
Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: 
No qualifying bean of type [com.autowire.sample.Formatter] is defined: 
expected single matching bean but found 2: barFormatter,fooFormatter
```
We can avoid this by narrowing the implementation using a @Qualifier annotation:
```java
public class FooService {
    @Autowired
    @Qualifier("fooFormatter")
    private Formatter formatter;
}
```
When there are multiple beans of the same type, it's a good idea to use @Qualifier to avoid ambiguity.

Please note that the value of the @Qualifier annotation matches with the name declared in 
the @Component annotation of our FooFormatter implementation.
#### 4.2. Autowiring by Name:
Spring uses the bean's name as a default qualifier value. 
It will inspect the container and look for a bean with the exact name as the property to autowire it.

Hence, in our example, Spring matches the fooFormatter property name to the FooFormatter implementation. 
Therefore, it injects that specific implementation when constructing FooService:
```java
public class FooService {
 @Autowired 
private Formatter fooFormatter; 
}
```
