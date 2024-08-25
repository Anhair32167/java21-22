package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;



import static telran.cars.dto.CarsReturnCode.*;

import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RemovedCarData;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyClerkControllerTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;

	@Test
	void testAddDriver() throws Exception {
		Driver driver = new Driver();		
		when(service.addDriver(driver)).thenReturn(OK);

		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("\"OK\"", actual);
	}

	@Test
	void testGetCar() throws Exception{
		Car car = new Car();
		when(service.getCar("111")).thenReturn(car);

		String actual = mock.perform(get("http://localhost:8080/car?regNumber=111")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(car), actual);
	}

	@Test
	void testGetModel() throws Exception{
		Model model = new Model();
		when(service.getModel("BMW X5")).thenReturn(model);

		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(model), actual);
	}

	@Test
	void testGetDriver() throws Exception{	
			Driver driver = new Driver();
			when(service.getDriver(111)).thenReturn(driver);
	
			String actual = mock.perform(get("http://localhost:8080/driver?licenceId=111")).andExpect(status().isOk()).andReturn()
					.getResponse().getContentAsString();
			
			assertEquals(mapper.writeValueAsString(driver), actual);
	}
		

	@Test
	void testRentCar()  throws Exception{	
		RentRecord data = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 15);
		when(service.rentCar(data.getRegNumber(), data.getLicenseId(), data.getRentDate(), data.getRentDays())).thenReturn(OK);

		String actual = mock.perform(post("http://localhost:8080/car/rent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("\"OK\"", actual);
	}

	@Test
	void testReturnCar() throws Exception{
		RentRecord data = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 15);
		RemovedCarData removed = new RemovedCarData();
		when(service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent())).thenReturn(removed);

		String actual = mock.perform(post("http://localhost:8080/car/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(mapper.writeValueAsString(removed), actual);
	}

	@Test
	void testGetModelNames() throws Exception{
		List<String> modelsRes = new ArrayList<>(List.of("mods"));
		when(service.getModelNames()).thenReturn(modelsRes);

		String actual = mock.perform(get("http://localhost:8080/models")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(modelsRes), actual);
	}

	@Test
	void testGetCarsByModel() throws Exception{
		Car car = new Car();
		List<Car> cars = new ArrayList<>(List.of(car));
		when(service.getCarsByModel("BMW X5")).thenReturn(cars);

		String actual = mock.perform(get("http://localhost:8080/cars/BMW X5")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(cars), actual);
	}

}
