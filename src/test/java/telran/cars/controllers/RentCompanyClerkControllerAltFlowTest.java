package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.cars.api.RentCompanyErrorMessages.*;
import static telran.cars.dto.CarsReturnCode.OK;

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

import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RemovedCarData;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyClerkControllerAltFlowTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;

//===============Add driver=====================
	@Test
	void testAddDriverNameNull() throws Exception{
		Driver driver = new Driver(0, null, 2000, "1111111");	
		when(service.addDriver(driver)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));

		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	
	@Test
	void testAddDriverNameBlank() throws Exception{
		Driver driver = new Driver(0, "", 2000, "1111111");	
		when(service.addDriver(driver)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	@Test
	void testAddDriverWrongAge() throws Exception{
		Driver driver = new Driver(0, "name", 2024, "1111111");	
		when(service.addDriver(driver)).thenThrow(new IllegalArgumentException(AGE_LESS_THAN_MIN));
		
		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_LESS_THAN_MIN, actual);
	}
	@Test
	void testAddDriverPhoneNull() throws Exception{
		Driver driver = new Driver(0, "name", 2024, null);	
		when(service.addDriver(driver)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));
		
		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	@Test
	void testAddDriverIdNegative() throws Exception{
		Driver driver = new Driver(-100, "name", 2024, null);	
		when(service.addDriver(driver)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NEGATIVE));
		
		String actual = mock.perform(post("http://localhost:8080/driver/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(driver)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NEGATIVE, actual);
	}
	
	
	
//===============Get car=====================
	@Test
	void testGetCarRegNull() throws Exception{
		Car car = new Car(null, "red", "BMW X5");
		when(service.getCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));

		String actual = mock.perform(get("http://localhost:8080/car?regNumber=111")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	@Test
	void testGetCarColourBlank() throws Exception{
		Car car = new Car("111", "", "BMW X5");
		when(service.getCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(get("http://localhost:8080/car?regNumber=111")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	@Test
	void testGetCarModelNull() throws Exception{
		Car car = new Car("111", "red", null);
		when(service.getCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));
		
		String actual = mock.perform(get("http://localhost:8080/car?regNumber=111")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	
//===============Get model=====================
	@Test
	void testGetModelNameNull() throws Exception{
		Model model = new Model(null, 50, "comp", "country", 30);
		when(service.getModel("BMW X5")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));

		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	@Test
	void testGetModelTankWrong() throws Exception{
		Model model = new Model("BMW X5", 100, "comp", "country", 30);
		when(service.getModel("BMW X5")).thenThrow(new IllegalArgumentException(AGE_GREATER_THAN_MAX));
		
		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(AGE_GREATER_THAN_MAX, actual);
	}
	@Test
	void testGetModelCompanyBlank() throws Exception{
		Model model = new Model("BMW X5", 50, "", "country", 30);
		when(service.getModel("BMW X5")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	@Test
	void testGetModelCountryNull() throws Exception{
		Model model = new Model("BMW X5", 50, "comp", null, 30);
		when(service.getModel("BMW X5")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));
		
		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	@Test
	void testGetModelPriceWrong() throws Exception{
		Model model = new Model("BMW X5", 50, "comp", "country", 6);
		when(service.getModel("BMW X5")).thenThrow(new IllegalArgumentException(AGE_LESS_THAN_MIN));
		
		String actual = mock.perform(get("http://localhost:8080/model?modelName=BMW X5")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(AGE_LESS_THAN_MIN, actual);
	}
//===============Get driver=====================
	@Test
	void testGetDriverIDNegative() throws Exception{
		String actual = mock.perform(get("http://localhost:8080/driver?licenceId=-111")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NEGATIVE, actual);		

	}
	
	@Test
	void testGetDriverIDMismatch() throws Exception{
		String actual = mock.perform(get("http://localhost:8080/driver?licenceId=aaa")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(TYPE_MISMATCH, actual);		
		
	}
	
//===============Return Car=====================

	@Test
	void testReturnCarRegBlank() throws Exception{
		RentRecord data = new RentRecord("", 111, LocalDate.of(2024, 1, 1), 15);
		//RemovedCarData removed = new RemovedCarData();
		when(service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent()))
		.thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));

		String actual = mock.perform(post("http://localhost:8080/car/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);	
	}
	@Test
	void testReturnCarLicenceIDNegative() throws Exception{
		RentRecord data = new RentRecord("111", -111, LocalDate.of(2024, 1, 1), 15);
		//RemovedCarData removed = new RemovedCarData();
		when(service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent()))
		.thenThrow(new IllegalArgumentException(ARGUMENT_IS_NEGATIVE));
		
		String actual = mock.perform(post("http://localhost:8080/car/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NEGATIVE, actual);	
	}
	@Test
	void testReturnCarDateFuture() throws Exception{
		RentRecord data = new RentRecord("111", 111, LocalDate.of(2034, 1, 1), 15);
		//RemovedCarData removed = new RemovedCarData();
		when(service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent()))
		.thenThrow(new IllegalArgumentException(DATE_IS_NOT_PAST));
		
		String actual = mock.perform(post("http://localhost:8080/car/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(DATE_IS_NOT_PAST, actual);	
	}
	@Test
	void testReturnCarWrongNumberOfDays() throws Exception{
		RentRecord data = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 115);
		//RemovedCarData removed = new RemovedCarData();
		when(service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent()))
		.thenThrow(new IllegalArgumentException(AGE_GREATER_THAN_MAX));
		
		String actual = mock.perform(post("http://localhost:8080/car/return")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_GREATER_THAN_MAX, actual);	
	}
	
//===============Rent Car=====================

	@Test
	void testRentCar() throws Exception{
		RentRecord data = new RentRecord("", 111, LocalDate.of(2024, 1, 1), 15);
		when(service.rentCar(data.getRegNumber(), data.getLicenseId(), data.getRentDate(), data.getRentDays()))
		.thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));

		String actual = mock.perform(post("http://localhost:8080/car/rent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);	
	}
	
//===============Get cars by model=====================	
	

	@Test
	void testGetCarsByModelBlank() throws Exception{

		String actual = mock.perform(get("http://localhost:8080/cars/ ")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}	

}
