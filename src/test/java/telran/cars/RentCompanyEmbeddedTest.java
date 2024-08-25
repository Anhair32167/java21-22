package telran.cars;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RentRecord;
import telran.cars.service.IRentCompany;

import static telran.cars.dto.CarsReturnCode.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
class RentCompanyEmbeddedTest {
	// static final String FILE_NAME = "company.data";

	static final String MODEL_NAME = "BMW X5";
	static final String REG_NUMBER = "123";
	static final long LICENSE_ID = 123;
	static final LocalDate RENT_DATE = LocalDate.of(2020, 10, 10);

	static Model model = new Model(MODEL_NAME, 50, "BMW", "Germany", 100);
	static Car car = new Car(REG_NUMBER, "red", MODEL_NAME);
	static Driver driver = new Driver(LICENSE_ID, "Max", 1980, "223322223");

	IRentCompany company;

	@Autowired
	ApplicationContext context;

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception
//	{
//		company = new RentCompanyEmbedded();
//		company.addModel(model);
//		company.addCar(car);
//		company.addDriver(driver);
//		company.rentCar(REG_NUMBER, LICENSE_ID, RENT_DATE, 10);
//		((RentCompanyEmbedded)company).save(FILE_NAME);
//	}

	@BeforeEach
	void setUp() throws Exception {
		company = context.getBean(IRentCompany.class);
		company.addModel(model);
		company.addCar(car);
		company.addDriver(driver);
		company.rentCar(REG_NUMBER, LICENSE_ID, RENT_DATE, 10);

	}

	@Test
	void testAddModel() {
		assertEquals(WRONG_DATA, company.addModel(null));
		assertEquals(MODEL_EXISTS, company.addModel(model));
		assertEquals(OK, company.addModel(new Model(MODEL_NAME + "2", 50, "BMW", "Germany", 100)));
	}

	@Test
	void testAddCar() {
		assertEquals(WRONG_DATA, company.addCar(null));
		assertEquals(NO_MODEL, company.addCar(new Car(REG_NUMBER + "1", "red", MODEL_NAME + "2")));
		assertEquals(CAR_EXISTS, company.addCar(car));
		assertEquals(OK, company.addCar(new Car(REG_NUMBER + "2", "red", MODEL_NAME)));
	}

	@Test
	void testAddDriver() {
		assertEquals(WRONG_DATA, company.addDriver(null));
		assertEquals(DRIVER_EXISTS, company.addDriver(driver));
		assertEquals(OK, company.addDriver(new Driver(LICENSE_ID + 2, "Max", 1980, "223322223")));
	}

	@Test
	void testGetModel() {
		assertNull(company.getModel(null));
		assertNull(company.getModel(MODEL_NAME + "5"));
		assertEquals(model, company.getModel(MODEL_NAME));
	}

	@Test
	void testGetCar() {
		assertNull(company.getCar(null));
		assertNull(company.getCar(REG_NUMBER + "5"));
		assertEquals(car, company.getCar(REG_NUMBER));
	}

	@Test
	void testGetDriver() {
		assertNull(company.getDriver(0));
		assertNull(company.getDriver(-1));
		assertNull(company.getDriver(LICENSE_ID + 5));
		assertEquals(driver, company.getDriver(LICENSE_ID));
	}

	@Test
	void testRentCar() {
		assertEquals(WRONG_DATA, company.rentCar(null, LICENSE_ID, RENT_DATE, 10));
		assertEquals(WRONG_DATA, company.rentCar(REG_NUMBER, 0, RENT_DATE, 10));
		assertEquals(WRONG_DATA, company.rentCar(REG_NUMBER, LICENSE_ID, null, 10));
		assertEquals(WRONG_DATA, company.rentCar(REG_NUMBER, LICENSE_ID, RENT_DATE, 0));
		assertEquals(NO_CAR, company.rentCar(REG_NUMBER + "5", LICENSE_ID, RENT_DATE, 10));
		assertEquals(NO_DRIVER, company.rentCar(REG_NUMBER, LICENSE_ID + 5, RENT_DATE, 10));
		assertEquals(CAR_IN_USE, company.rentCar(REG_NUMBER, LICENSE_ID, RENT_DATE, 10));

		Car car7 = new Car(REG_NUMBER+"7", "red", MODEL_NAME);
		company.addCar(car7);
		car7.setFlagRemoved(true);
		assertEquals(CAR_REMOVED, company.rentCar(REG_NUMBER+"7", LICENSE_ID, RENT_DATE, 10));

		Car car2 = new Car(REG_NUMBER + "2", "red", MODEL_NAME);
		company.addCar(car2);
		assertEquals(OK, company.rentCar(REG_NUMBER + "2", LICENSE_ID, RENT_DATE, 10));
	}

	@Test
	void testGetCarsByDriver() {
		Set<Car> set = company.getCarsByDriver(LICENSE_ID);
		Car car1 = new Car(REG_NUMBER + "1", "red", MODEL_NAME);
		assertEquals(1, set.size());
		assertTrue(set.contains(car1));

		set = company.getCarsByDriver(LICENSE_ID + 5);
		assertTrue(set.isEmpty());
	}

	@Test
	void testGetDriversByCar() {
		Set<Driver> set = company.getDriversByCar(REG_NUMBER + "1");
		assertEquals(1, set.size());
		assertTrue(set.contains(driver));

		set = company.getDriversByCar(REG_NUMBER + "5");
		assertTrue(set.isEmpty());
	}
	
	@Test
	void testGetCarsByModel()
	{
		List<Car> list = company.getCarsByModel(MODEL_NAME);//in use
		assertTrue(list.isEmpty());
		
		list = company.getCarsByModel(MODEL_NAME+"1");
		assertTrue(list.isEmpty());
		
		Car car3 = new Car(REG_NUMBER+3, "red", MODEL_NAME);
		company.addCar(car3);
		list = company.getCarsByModel(MODEL_NAME);
		assertEquals(1, list.size());
		assertTrue(list.contains(car3));
	}
	
	@Test
	void testGetRentRecordsAtDates()
	{
		RentRecord exp = new RentRecord(REG_NUMBER+"1", LICENSE_ID, RENT_DATE, 10);
		List<RentRecord> list = company.getRentRecordsAtDates(LocalDate.of(1990, 1, 1), 
				LocalDate.of(2022, 1, 1));
		assertEquals(2, list.size());
		assertTrue(list.contains(exp));
		
		list = company.getRentRecordsAtDates(LocalDate.of(1990, 1, 1), 
				LocalDate.of(1999, 1, 1));
		assertTrue(list.isEmpty());
		
		list = company.getRentRecordsAtDates(LocalDate.of(1990, 1, 1), 
				RENT_DATE);
		assertTrue(list.isEmpty());
		
	}
}
