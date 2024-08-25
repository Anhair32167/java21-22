package telran.cars.api;

public interface RentCompanyErrorMessages {
	//String DATE_IS_NULL = "Date can't be null";s
	String ARGUMENT_IS_NULL = "Argument can't be null";
	String ARGUMENT_IS_NEGATIVE = "Argument can't negative";
	String ARGUMENT_IS_BLANK = "Argument can't be blank";
	String DATE_IS_NOT_PAST = "Date must be in the past";
	String DATE_WRONG_FORMAT = "Wrong date format";
	String AGE_LESS_THAN_MIN = "Age must be greater or equal min";
	String AGE_GREATER_THAN_MAX = "Age must be less or equal max";
	String TYPE_MISMATCH = "Url parameter has type mismatch";
	String JSON_TYPE_MISMATCH = "JSON contains field with type mismatch";
	

}
