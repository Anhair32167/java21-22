package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyTechnitianControllerTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;
	@Test
	void testGetCarsByDriver() throws Exception {
		Car car = new Car();
		Set<Car> cars = new HashSet<>(Set.of(car));
		when(service.getCarsByDriver(111)).thenReturn(cars);

		String actual = mock.perform(get("http://localhost:8080/driver/111/cars")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(cars), actual);
	}

	@Test
	void testGetDriversByCar() throws Exception {
		Driver driver = new Driver();
		Set<Driver> drivers = new HashSet<>(Set.of(driver));
		when(service.getDriversByCar("111")).thenReturn(drivers);

		String actual = mock.perform(get("http://localhost:8080/car/111/drivers")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(drivers), actual);
	}

	@Test
	void testGetRentRecordsAtDates() throws Exception {
		RentRecord record = new RentRecord();
		List<RentRecord> records = new ArrayList<>(List.of(record));
		when(service.getRentRecordsAtDates(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 1, 1))).thenReturn(records);

		String actual = mock.perform(get("http://localhost:8080/records/1900-01-01/2030-01-01")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(mapper.writeValueAsString(records), actual);
	}

}
