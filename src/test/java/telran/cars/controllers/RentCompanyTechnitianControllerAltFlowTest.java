package telran.cars.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.cars.api.RentCompanyErrorMessages.*;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.cars.dto.Car;
import telran.cars.service.IRentCompany;
@WebMvcTest
class RentCompanyTechnitianControllerAltFlowTest {
	@MockBean
	IRentCompany service;

	@Autowired
	MockMvc mock;
	@Autowired
	ObjectMapper mapper;

	
//===============Get cars by driver=====================
	@Test
	void testGetCarsByDriverIdNegative() throws Exception {		

		String actual = mock.perform(get("http://localhost:8080/driver/-111/cars")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_NEGATIVE, actual);	
	}
	
	@Test
	void testGetCarsByDriverIdMismatch() throws Exception {		
		
		String actual = mock.perform(get("http://localhost:8080/driver/aaa/cars")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(TYPE_MISMATCH, actual);	
	}
	
//===============Get driver by car=====================

	@Test
	void testGetDriversByCarBlank() throws Exception{
		String actual = mock.perform(get("http://localhost:8080/car/ /drivers")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(ARGUMENT_IS_BLANK, actual);	
	}
	
//===============Get driver by car=====================

	@Test
	void testGetRentRecordsAtDatesWrongFormat() throws Exception{
		String actual = mock.perform(get("http://localhost:8080/records/01-01-1900/2030-01-01")).andExpect(status().isBadRequest()).andReturn()
				.getResponse().getContentAsString();
		
		assertEquals(DATE_WRONG_FORMAT, actual);	
	}

}
