package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.cars.api.RentCompanyErrorMessages.*;
import static telran.cars.dto.CarsReturnCode.OK;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.Car;
import telran.cars.dto.Model;
import telran.cars.dto.RemovedCarData;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyAdministratorControllerAltFlowTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;
	
//===============Add car=====================	
	@Test
	void testAddCarRegBlank() throws Exception {
		Car car = new Car(" ", "red", "BMW X5");		
		when(service.addCar(car)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));

		String actual = mock.perform(post("http://localhost:8080/car/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testAddCarColourBlank() throws Exception {
		Car car = new Car("111", " ", "BMW X5");		
		when(service.addCar(car)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(post("http://localhost:8080/car/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testAddCarModelBlank() throws Exception {
		Car car = new Car("111", "red", " ");		
		when(service.addCar(car)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(post("http://localhost:8080/car/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
//===============Add model=====================

	@Test
	void testAddModelNameBlank() throws Exception {
		Model model = new Model(" ", 50, "comp", "count", 30);
		when(service.addModel(model)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));

		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testAddModelTankWrong() throws Exception {
		Model model = new Model("BMW X5", 150, "comp", "count", 30);
		when(service.addModel(model)).thenThrow(new IllegalArgumentException(AGE_GREATER_THAN_MAX));
		
		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_GREATER_THAN_MAX, actual);
	}
	
	@Test
	void testAddModelCompBlank() throws Exception {
		Model model = new Model("BMW X5", 50, " ", "count", 30);
		when(service.addModel(model)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testAddModelCuntryBlank() throws Exception {
		Model model = new Model("BMW X5", 50, "comp", " ", 30);
		when(service.addModel(model)).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));
		
		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testAddModelPriceWrong() throws Exception {
		Model model = new Model("BMW X5", 50, "comp", "country", 10);
		when(service.addModel(model)).thenThrow(new IllegalArgumentException(AGE_LESS_THAN_MIN));
		
		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_LESS_THAN_MIN, actual);
	}

//===============Remove car=====================
	
	@Test
	void testRemoveCarRegBlank() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord(" ", 111, LocalDate.of(2024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_BLANK));

		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_BLANK, actual);
	}
	
	@Test
	void testRemoveCarIdNegative() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", -111, LocalDate.of(2024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NEGATIVE));
		
		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NEGATIVE, actual);
	}
	@Test
	void testRemoveCarDateNull() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, null, 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));
		
		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NULL, actual);
	}
	@Test
	void testRemoveCarDateInFuture() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, LocalDate.of(3024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenThrow(new IllegalArgumentException(DATE_IS_NOT_PAST));
		
		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(DATE_IS_NOT_PAST, actual);
	}
	@Test
	void testRemoveCarRentDaysWrong() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 110);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenThrow(new IllegalArgumentException(AGE_GREATER_THAN_MAX));
		
		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_GREATER_THAN_MAX, actual);
	}

//===============Remove model=====================

	@Test
	void testRemoveModelNull() throws Exception {
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(null, records);
		List<RemovedCarData> removedList = new ArrayList<>(List.of(removed));
		when(service.removeModel("BMW X5")).thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));

		String actual = mock.perform(delete("http://localhost:8080/model/remove?modelName=BMW X5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NULL, actual);
	}

}
