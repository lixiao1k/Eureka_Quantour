package presentation.chart.function;

import java.time.LocalDate;
import java.util.List;

public interface ListToArrayService {
	
	/**
	 * @Description: change Double list to array without sorting
	 * @author: hzp
	 * @time: 2017年4月13日
	 * @return: Double[]
	 */
	Double[] changeDouble(List<Double> list);

	/**
	 * @Description: change LocalDate list to array without sorting
	 * @author: hzp
	 * @time: 2017年4月15日
	 * @return: LocalDate[]
	 */
	LocalDate[] changeLocalDate(List<LocalDate> list);
	
	/**
	 * @Description: format LocalDate to String and change list storage to array storage
	 * @author: hzp
	 * @time: 2017年4月15日
	 * @return: String[]
	 */
	String[] formatLocalDate(List<LocalDate> list);
	
	/**
	 * @Description: format LocalDate which stored in array to String
	 * @author: hzp
	 * @time: 2017年4月15日
	 * @return: String[]
	 */
	String[] formatLocalDate(LocalDate[] list);
	
	/**
	 * @Description: format Integer to String
	 * @author: hzp
	 * @time: 2017年4月13日
	 * @return: String[]
	 */
	String[] formatInteger(int[] list);
	
	/**
	 * @Description: format Double to String and change list storage to array storage
	 * @author: hzp
	 * @time: 2017年4月13日
	 * @return: String[]
	 */
	String[] formatDouble(List<Double> list);
	
	/**
	 * @Description: change Double to double
	 * @author: hzp
	 * @time: 2017年4月13日
	 * @return: double[]
	 */
	double[] DTod(Double[] list);
	
	/**
	 * @Description: change double to Double
	 * @author: hzp
	 * @time: 2017年4月13日
	 * @return: Double[]
	 */
	Double[] dToD(double[] list);
}
