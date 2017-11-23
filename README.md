spring-enricher
============

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-enricher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.avides.spring/spring-enricher)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/214f252432944f9ea163be71e9a775c2)](https://www.codacy.com/app/avides-builds/spring-enricher?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=avides/spring-enricher&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/avides/spring-enricher/badge.svg?branch=master)](https://coveralls.io/github/avides/spring-enricher?branch=master)
[![Build Status](https://travis-ci.org/avides/spring-enricher.svg?branch=master)](https://travis-ci.org/avides/spring-enricher)

#### Maven
```xml
<dependency>
    <groupId>com.avides.spring</groupId>
    <artifactId>spring-enricher</artifactId>
    <version>1.0.1.RELEASE</version>
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
