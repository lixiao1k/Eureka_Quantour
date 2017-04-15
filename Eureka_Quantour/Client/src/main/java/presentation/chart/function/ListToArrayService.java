package presentation.chart.function;

import java.time.LocalDate;
import java.util.List;

public interface ListToArrayService {
	
	Double[] changeDouble(List<Double> list);

	LocalDate[] changeLocalDate(List<LocalDate> list);
	
	String[] formatLocalDate(List<LocalDate> list);
	
	String[] formatLocalDate(LocalDate[] list);
	
	String[] formatInteger(int[] list);
	
	String[] formatDouble(List<Double> list);
	
	double[] DTod(Double[] list);
	
	Double[] dToD(double[] list);
}
