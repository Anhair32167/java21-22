package telran.cars.dto;

import static telran.cars.api.RentCompanyErrorMessages.AGE_GREATER_THAN_MAX;
import static telran.cars.api.RentCompanyErrorMessages.AGE_LESS_THAN_MIN;
import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_BLANK;
import static telran.cars.api.RentCompanyErrorMessages.ARGUMENT_IS_NULL;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Driver implements Serializable
{
	private static final long serialVersionUID = -4101486848309936165L;
	@Positive
	private long licenseId;
	@NotNull(message = ARGUMENT_IS_NULL)
	@NotBlank(message = ARGUMENT_IS_BLANK)
	private String name;
	@Pattern(regexp = "\\d{4}")
	@Min(value = 1944, message = AGE_LESS_THAN_MIN)
	@Min(value = 2006, message = AGE_GREATER_THAN_MAX)
	private int birthYear;
	@NotNull(message = ARGUMENT_IS_NULL)
	@NotBlank(message = ARGUMENT_IS_BLANK)
	private String phone;
	
	public Driver()
	{
	}

	public Driver(long licenseId, String name, int birthYear, String phone)
	{
		super();
		this.licenseId = licenseId;
		this.name = name;
		this.birthYear = birthYear;
		this.phone = phone;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public long getLicenseId()
	{
		return licenseId;
	}

	public String getName()
	{
		return name;
	}

	public int getBirthYear()
	{
		return birthYear;
	}

	@Override
	public String toString()
	{
		return "Driver [licenseId=" + licenseId + ", name=" + name + ", birthYear=" + birthYear + ", phone=" + phone
				+ "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (licenseId ^ (licenseId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Driver))
			return false;
		Driver other = (Driver) obj;
		if (licenseId != other.licenseId)
			return false;
		return true;
	}
}
