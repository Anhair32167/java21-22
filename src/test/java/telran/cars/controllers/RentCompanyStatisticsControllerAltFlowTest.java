package telran.cars.controllers;
import static telran.cars.api.RentCompanyErrorMessages.*;
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


import telran.cars.dto.StatisticsData;
import telran.cars.service.IRentCompany;

@WebMvcTest
class RentCompanyStatisticsControllerAltFlowTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;
//===============Popular cars=====================
	@Test
	void testGetMostPopularCarModelsDateNull() throws Exception {	
		StatisticsData data = new StatisticsData(null, LocalDate.of(2030, 1, 1), 20,40);		
		when(service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(), data.getToAge()))
		.thenThrow(new IllegalArgumentException(ARGUMENT_IS_NULL));
		
		String actual = mock.perform(post("http://localhost:8080/models/popular")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(ARGUMENT_IS_NULL, actual);
		
	}
	
	@Test
	void testGetMostPopularCarModelsDateFuture() throws Exception {	
		StatisticsData data = new StatisticsData(LocalDate.of(2031, 1, 1), LocalDate.of(2030, 1, 1), 20,40);		
		when(service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(), data.getToAge()))
		.thenThrow(new IllegalArgumentException(DATE_IS_NOT_PAST));
		
		String actual = mock.perform(post("http://localhost:8080/models/popular")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(DATE_IS_NOT_PAST, actual);
		
	}
	@Test
	void testGetMostPopularCarModelsAgeGreaterMax() throws Exception {	
		StatisticsData data = new StatisticsData(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 1, 1), 20,90);		
		when(service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(), data.getToAge()))
		.thenThrow(new IllegalArgumentException(AGE_GREATER_THAN_MAX));
		
		String actual = mock.perform(post("http://localhost:8080/models/popular")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_GREATER_THAN_MAX, actual);
		
	}
	@Test
	void testGetMostPopularCarModelsAgeLessMin() throws Exception {	
		StatisticsData data = new StatisticsData(LocalDate.of(1900, 1, 1), LocalDate.of(2030, 1, 1), 5,40);		
		when(service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(), data.getToAge()))
		.thenThrow(new IllegalArgumentException(AGE_LESS_THAN_MIN));
		
		String actual = mock.perform(post("http://localhost:8080/models/popular")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(data)))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(AGE_LESS_THAN_MIN, actual);
		
	}
		
	//===============Profitable cars=====================
	@Test
	void testGetMostProfitableCarModelsWrongPattern() throws Exception {		
		String actual = mock.perform(get("http://localhost:8080/models/profitable/01-01-1900/2030-01-01"))				
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertEquals(DATE_WRONG_FORMAT, actual);
	}
//	@Test
//	void testGetMostProfitableCarModelsPast() throws Exception {		
//		String actual = mock.perform(get("http://localhost:8080/models/profitable/2031-01-01/2030-01-01"))				
//				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
//		assertEquals(DATE_IS_NOT_PAST, actual);
//	}
	
	



}
