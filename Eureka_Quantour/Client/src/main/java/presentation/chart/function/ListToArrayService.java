package presentation.chart.function;

import java.util.Calendar;
import java.util.List;

public interface ListToArrayService {
	
	Double[] changeDouble(List<Double> list);

	Calendar[] changeCalendar(List<Calendar> list);
	
	String[] formatCalendar(List<Calendar> list);
	
	String[] formatCalendar(Calendar[] list);
	
	String[] formatInteger(int[] list);
	
	String[] formatDouble(List<Double> list);
	
	double[] DTod(Double[] list);
	
	Double[] dToD(double[] list);
}
