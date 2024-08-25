package telran.cars.controllers;

import static telran.cars.api.RentCompanyErrorMessages.*;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.Driver;
import telran.cars.dto.StatisticsData;
import telran.cars.service.IRentCompany;

@RestController
@Slf4j
public class RentCompanyStatisticsController {
	@Autowired
	IRentCompany service;

	private <T> void logging(String methodName, T data) {
		log.debug(String.format("%s: receiver data: {}", data));
	}

	@GetMapping("/drivers/active")
	List<Driver> getMostActiveDrivers() {
		logging("getMostActiveDrivers", null);
		return service.getMostActiveDrivers();
	}

	@PostMapping("/models/popular")
	// @DateTimeFormat(iso = ISO.DATE)
	List<String> getMostPopularCarModels(@RequestBody @Valid StatisticsData data) {
		logging("getMostPopularCarModels", data);
		return service.getMostPopularCarModels(data.getFromDate(), data.getToDate(), data.getFromAge(),
				data.getToAge());
	}

	@GetMapping("/models/profitable/{dateFrom}/{dateTo}")
	@DateTimeFormat(iso = ISO.DATE)
	List<String> getMostProfitableCarModels(
			@PathVariable	
			@Pattern(regexp = "\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([12][0-9])|(3[01]))", message = DATE_WRONG_FORMAT) 
			String dateFrom,
			@PathVariable		
			@Pattern(regexp = "\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([12][0-9])|(3[01]))", message = DATE_WRONG_FORMAT) 
			String dateTo) {
		@Past(message = DATE_IS_NOT_PAST) 
		LocalDate fromDate = LocalDate.parse(dateFrom);
		LocalDate toDate = LocalDate.parse(dateTo);

		logging("getMostProfitableCarModels", new StatisticsData(fromDate, toDate, 0, 0));
		return service.getMostProfitableCarModels(fromDate, toDate);
	}

}
