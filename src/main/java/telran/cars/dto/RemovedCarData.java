package telran.cars.dto;

import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_NULL;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class RemovedCarData implements Serializable
{
	private static final long serialVersionUID = -7682375091125886841L;

	@NotNull(message = ARGUMENT_IS_NULL)
	private Car car;
	private List<RentRecord> removedRecords;
	
	public RemovedCarData()
	{
	}

	public RemovedCarData(Car car, List<RentRecord> removedRecords)
	{
		super();
		this.car = car;
		this.removedRecords = removedRecords;
	}

	public Car getCar()
	{
		return car;
	}

	public List<RentRecord> getRemovedRecords()
	{
		return removedRecords;
	}

	@Override
	public String toString()
	{
		return "RemovedCarData [car=" + car + ", removedRecords=" + removedRecords + "]";
	}
}
