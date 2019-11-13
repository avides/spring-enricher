# spring-enricher

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-enricher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-enricher)
[![Build](https://github.com/avides/spring-enricher/workflows/release/badge.svg)](https://github.com/avides/spring-enricher/actions)
[![Nightly build](https://github.com/avides/spring-enricher/workflows/nightly/badge.svg)](https://github.com/avides/spring-enricher/actions)
[![Coverage report](https://sonarcloud.io/api/project_badges/measure?project=avides_spring-enricher&metric=coverage)](https://sonarcloud.io/dashboard?id=avides_spring-enricher)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=avides_spring-enricher&metric=alert_status)](https://sonarcloud.io/dashboard?id=avides_spring-enricher)
[![Technical dept](https://sonarcloud.io/api/project_badges/measure?project=avides_spring-enricher&metric=sqale_index)](https://sonarcloud.io/dashboard?id=avides_spring-enricher)

#### Maven
```xml
<dependency>
    <groupId>com.avides.spring</groupId>
    <artifactId>spring-enricher</artifactId>
    <version>2.0.0</version>
</dependency>
```
#### Example
```java
@Configuration
@EnableEnriching
public class EnricherConfiguration
{
}

@Service
public class CustomerService
{
    @Autowired
    private CustomerRepository customerRepository;

    @Enriched
    public Customer getCustomer(long id)
    {
        return customerRepository.getCustomer(id);
    }

    @Enriched
    public List<Customer> getCustomers()
    {
        return customerRepository.getCustomers();
    }
}

@Component
public class CustomerEnricher extends AbstractEnricher<Customer>
{
    @Autowired
    private AddressService addressService;

    public CustomerEnricher()
    {
        super(Customer.class);
    }

    @Override
    public void doEnrich(Customer customer)
    {
        customer.setAddresses(addressService.getAddresses(customer.getId()));
    }
}

@Component
public class CustomerOutput
{
    @Autowired
    private CustomerService customerService;

    public void outputCustomers()
    {
        for (Customer customer : customerService.getCustomers())
        {
            System.out.println(customer);
        }
    }

    public void outputCustomer(long id)
    {
        System.out.println(customerService.getCustomer(id));
    }
}
```
