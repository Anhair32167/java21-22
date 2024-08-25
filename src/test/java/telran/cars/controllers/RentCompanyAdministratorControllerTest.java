package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import static telran.cars.dto.CarsReturnCode.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import telran.cars.dto.Car;
import telran.cars.dto.Model;
import telran.cars.dto.RemovedCarData;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyAdministratorControllerTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;
	
	@Test
	void testAddCar() throws Exception{
		Car car = new Car("111", "red", "BMW X5");		
		when(service.addCar(car)).thenReturn(OK);

		String actual = mock.perform(post("http://localhost:8080/car/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("\"OK\"", actual);
	}

	@Test
	void testAddModel() throws Exception{
		Model model = new Model("BMW X5", 50, "comp", "count", 30);
		when(service.addModel(model)).thenReturn(OK);

		String actual = mock.perform(post("http://localhost:8080/model/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(model)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();		
		assertEquals("\"OK\"", actual);
	}

	@Test
	void testRemoveCar() throws Exception{
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		when(service.removeCar("111")).thenReturn(removed);

		String actual = mock.perform(delete("http://localhost:8080/car/remove?regNumber=111")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	
		assertEquals(mapper.writeValueAsString(removed), actual);
	}

	@Test
	void testRemoveModel() throws Exception{
		Car car = new Car("111", "red", "BMW X5");
		RentRecord record = new RentRecord("111", 111, LocalDate.of(2024, 1, 1), 10);
		List<RentRecord> records = new ArrayList<>(List.of(record));
		RemovedCarData removed = new RemovedCarData(car, records);
		List<RemovedCarData> removedList = new ArrayList<>(List.of(removed));
		when(service.removeModel("BMW X5")).thenReturn(removedList);

		String actual = mock.perform(delete("http://localhost:8080/model/remove?modelName=BMW X5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(car)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(mapper.writeValueAsString(removedList), actual);
	}

}
