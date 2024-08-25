package telran.cars.controllers;

import static telran.cars.api.RentCompanyErrorMessages.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import telran.cars.dto.Car;
import telran.cars.dto.CarsReturnCode;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RemovedCarData;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;

@RestController
@Slf4j
public class RentCompanyClerkController {
	@Autowired
	IRentCompany service;
	
	private <T> void logging(String methodName, T data) {
		log.debug(String.format("%s: receiver data: {}", data));
	}
	
	@PostMapping("/driver/add")
	CarsReturnCode addDriver(@RequestBody Driver driver) {
		logging("addDriver", driver);		
		return service.addDriver(driver);
	}
	
	@GetMapping("/car")
	Car getCar(
			@RequestParam
			@NotNull(message = ARGUMENT_IS_NULL)
			@NotBlank(message = ARGUMENT_IS_BLANK)
			String regNumber) {
		logging("getCar", regNumber);		
		return service.getCar(regNumber);
	}
	
	@GetMapping("/model")
	Model getModel(
			@RequestParam 
			@NotNull(message = ARGUMENT_IS_NULL)
			@NotBlank(message = ARGUMENT_IS_BLANK)
			String modelName) {
		logging("getModel", modelName);		
		return service.getModel(modelName);
	}
	
	@GetMapping("/driver")
	Driver getDriver(
			@RequestParam 
			@Positive(message = ARGUMENT_IS_NEGATIVE) 
			long licenceId) {
		logging("getDriver", licenceId);		
		return service.getDriver(licenceId);
	}
	
	@PostMapping("/car/rent")
	CarsReturnCode rentCar(@RequestBody @Valid  RentRecord data) {
		logging("rentCar", data);		
		return service.rentCar(data.getRegNumber(), data.getLicenseId(), data.getRentDate(), data.getRentDays());
	}
	
	@PostMapping("/car/return")
	RemovedCarData returnCar(@RequestBody @Valid  RentRecord data) {
		logging("returnCar", data);		
		return service.returnCar(data.getRegNumber(), data.getLicenseId(), data.getReturnDate(), data.getDamagesPercent(), data.getTankPercent());
	}
	
	@GetMapping("/models")
	List<String> getModelNames() {
		logging("getModelNames", null);		
		return service.getModelNames();
	}
	
	@GetMapping("cars/{modelName}")
	List<Car> getCarsByModel(
			@PathVariable 
			@NotNull(message = ARGUMENT_IS_NULL)
			@NotBlank(message = ARGUMENT_IS_BLANK)
			String modelName) {
		logging("getCarsByModel", modelName);		
		return service.getCarsByModel(modelName);
	}
}
