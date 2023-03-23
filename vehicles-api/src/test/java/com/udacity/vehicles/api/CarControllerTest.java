package com.udacity.vehicles.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
//@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;



    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    @MockBean
    private CarResourceAssembler assembler;


    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        // Given
        Car car = getCar();
        car.setId(1L);
        given(assembler.toResource(any())).willReturn(new Resource<>(car));
        given(carService.list()).willReturn(Collections.singletonList(car));

        // When
        MvcResult result = mvc.perform(get("/cars")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseContent);
        // Then
        // where 'json' is the JSON string
        JsonNode carNode = rootNode.path("_embedded").path("carList").get(0); // get the first car node
        Car returnedCar = mapper.treeToValue(carNode, Car.class);
        assertEquals(returnedCar.getId(), car.getId());
        assertEquals(returnedCar.getLocation().getAddress(), car.getLocation().getAddress());
        assertEquals(returnedCar.getLocation().getLat(), car.getLocation().getLat());
        assertEquals(returnedCar.getDetails().getBody(), car.getDetails().getBody());
        assertEquals(returnedCar.getDetails().getExternalColor(), car.getDetails().getExternalColor());
        assertEquals(returnedCar.getDetails().getEngine(), car.getDetails().getEngine());
        assertEquals(returnedCar.getDetails().getFuelType(), car.getDetails().getFuelType());
        assertEquals(returnedCar.getDetails().getModelYear(), car.getDetails().getModelYear());
        assertEquals(returnedCar.getDetails().getNumberOfDoors(), car.getDetails().getNumberOfDoors());
        assertEquals(returnedCar.getCondition(), car.getCondition());
    }


    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        // Given
        Car car = getCar();
        car.setId(1L);
        given(assembler.toResource(any())).willReturn(new Resource<>(car));
        given(carService.list()).willReturn(Collections.singletonList(car));
        given(carService.findById(anyLong())).willReturn(car);

        mvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.condition").value("USED"))
                .andExpect(jsonPath("$.details.model").value(car.getDetails().getModel()));

        verify(carService, times(1)).findById(anyLong());
    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar()  throws Exception {
        Car car = getCar();
        car.setId(1L);
        given(carService.findById(car.getId())).willReturn(car);
        mvc.perform(delete("/cars/{id}", car.getId()))
                .andExpect(status().isNoContent());
        verify(carService, times(1)).delete(car.getId());
    }


    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}