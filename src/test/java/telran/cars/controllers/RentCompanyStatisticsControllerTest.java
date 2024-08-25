package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import lombok.RequiredArgsConstructor;
import telran.cars.dto.Driver;
import telran.cars.dto.StatisticsData;
import telran.cars.service.IRentCompany;

@WebMvcTest
class RentCompanyStatisticsControllerTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;

	@Test
	void testGetMostActiveDrivers() throws Exception {

		List<Driver> driverRes = new ArrayList<>(List.of(new Driver()));
		when(service.getMostActiveDrivers()).thenReturn(driverRes);

		String actual = mock.perform(get("http://localhost:8080/drivers/active")).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertEquals(mapper.writeValueAsString(driverRes), actual);
	}

	@Test
	void testGetMostPopularCarModels() throws Exception {	
		StatisticsData data = new StatisticsData(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 1, 1), 20,40);
		List<String> modelsRes = new ArrayList<>(List.of("be"));
		when(service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(), data.getToAge())).thenReturn(modelsRes);
		
		String actual = mock.perform(post("http://localhost:8080/models/popular")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(mapper.writeValueAsString(modelsRes), actual);
		
	}
		

	@Test
	void testGetMostProfitableCarModels() throws Exception {
		StatisticsData data = new StatisticsData(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 1, 1), 20,40);
		List<String> modelsRes = new ArrayList<>(List.of("mods"));
		when(service.getMostProfitableCarModels(data.getFromDate(), data.getToDate())).thenReturn(modelsRes);
		
		String actual = mock.perform(get("http://localhost:8080/models/profitable/1900-01-01/2030-01-01"))				
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(mapper.writeValueAsString(modelsRes), actual);
	}
}


