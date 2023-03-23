package com.udacity.vehicles.test_config;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//
//@Configuration
//@Profile("test")
//public class TestConfig {
//
//    @Bean
//    CommandLineRunner initDatabaseTest(ManufacturerRepository repository) {
//        return args -> {
//            repository.save(new Manufacturer(100, "Audi"));
//            repository.save(new Manufacturer(101, "Chevrolet"));
//            repository.save(new Manufacturer(102, "Ford"));
//            repository.save(new Manufacturer(103, "BMW"));
//            repository.save(new Manufacturer(104, "Dodge"));
//        };
//    }
//    @Bean
//    CommandLineRunner initCarTest(CarRepository repository) {
//        Car car = new Car();
//        car.setLocation(new Location(40.730610, -73.935242));
//        Details details = new Details();
//        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
//        details.setManufacturer(manufacturer);
//        details.setModel("Impala");
//        details.setMileage(32280);
//        details.setExternalColor("white");
//        details.setBody("sedan");
//        details.setEngine("3.6L V6");
//        details.setFuelType("Gasoline");
//        details.setModelYear(2018);
//        details.setProductionYear(2018);
//        details.setNumberOfDoors(4);
//        car.setDetails(details);
//        car.setCondition(Condition.USED);
//        return args -> {
//            repository.save(car);
//
//        };
//    }
//}