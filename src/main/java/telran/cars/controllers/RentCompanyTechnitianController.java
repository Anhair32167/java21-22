package telran.cars.controllers;

import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_BLANK;
import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_NEGATIVE;
import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_NULL;
import static telran.cars.api.RentCompanyErrorMessages.DATE_IS_NOT_PAST;
import static telran.cars.api.RentCompanyErrorMessages.DATE_WRONG_FORMAT;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.RentRecord;
import telran.cars.dto.StatisticsData;
import telran.cars.service.IRentCompany;

@RestController
@Slf4j
public class RentCompanyTechnitianController {
	@Autowired
	IRentCompany service;
	
	private <T> void logging(String methodName, T data) {
		log.debug(String.format("%s: receiver data: {}", data));
	}
	
	@GetMapping("/driver/{licenseId}/cars")
	Set<Car> getCarsByDriver(
			@PathVariable 
			@Positive(message = ARGUMENT_IS_NEGATIVE)  
			long licenseId) {
		logging("getCarsByDriver", licenseId);		
		return service.getCarsByDriver(licenseId);
	}
	
	@GetMapping("/car/{regNumber}/drivers")
	Set<Driver> getDriversByCar(
			@PathVariable
			@NotBlank(message = ARGUMENT_IS_BLANK)			
			String regNumber) {
		logging("getDriversByCar", regNumber);		
		return service.getDriversByCar(regNumber);
	}
	
	@GetMapping("/records/{from}/{to}")
	List<RentRecord> getRentRecordsAtDates(
			@PathVariable			
			@Pattern(regexp = "\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([12][0-9])|(3[01]))", message = DATE_WRONG_FORMAT) 
			String from,
			@PathVariable			
			@Pattern(regexp = "\\d{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([12][0-9])|(3[01]))", message = DATE_WRONG_FORMAT) 
			String to) {
		@Past(message = DATE_IS_NOT_PAST)
		LocalDate fromDate = LocalDate.parse(from);
		LocalDate toDate = LocalDate.parse(to);
		
		logging("getMostProfitableCarModels", new StatisticsData(fromDate, toDate, 0, 0));
		return service.getRentRecordsAtDates(fromDate, toDate);
	}
	
	
}
