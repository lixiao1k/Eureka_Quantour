package presentation.chart.function;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListToArray implements ListToArrayService{

	private SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");
	private DecimalFormat df = new DecimalFormat("0.00000");   
	
	@Override
	public Double[] changeDouble(List<Double> list) {
		// TODO Auto-generated method stub
		Double[] d = new Double[list.size()];
		for( int i=0; i<d.length; i++)
			d[i] = list.get(i);
		return d;
	}

	@Override
	public Calendar[] changeCalendar(List<Calendar> list) {
		// TODO Auto-generated method stub
		Calendar[] cal = new Calendar[list.size()];
		for( int i=0; i<cal.length; i++){
			cal[i] = Calendar.getInstance();
			cal[i] = (Calendar)list.get(i).clone();
		}
		return cal;
	}

	@Override
	public String[] formatCalendar(List<Calendar> list) {
		// TODO Auto-generated method stub
		String[] cal = new String[list.size()];
		for( int i=0; i<cal.length; i++)
			cal[i] = sdf.format(list.get(i).getTime());
		return cal;
	}

	@Override
	public String[] formatCalendar(Calendar[] list) {
		// TODO Auto-generated method stub
		String[] cal = new String[list.length];
		for( int i=0; i<cal.length; i++)
			cal[i] = sdf.format(list[i].getTime());
		return cal;
	}

	@Override
	public String[] formatInteger(int[] list) {
		// TODO Auto-generated method stub
		String[] str = new String[list.length];
		for( int i=0; i<list.length; i++)
			str[i] = String.valueOf( list[i] );
		return str;
	}
	
	@Override
	public String[] formatDouble(List<Double> list) {
		// TODO Auto-generated method stub
		String[] str = new String[list.size()];
		for( int i=0; i<list.size(); i++)
			str[i] = df.format(list.get(i));
		return str;
	}

	@Override
	public double[] DTod(Double[] list) {
		// TODO Auto-generated method stub
		double[] d = new double[list.length];
		for( int i=0; i<list.length; i++)
			d[i] = list[i];
		return d;
	}

	@Override
	public Double[] dToD(double[] list) {
		// TODO Auto-generated method stub
		Double[] D = new Double[list.length];
		for( int i=0; i<list.length; i++)
			D[i] = list[i];
		return D;
	}
	
}
