package presentation.chart.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListToArray implements ListToArrayService{

	private SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd");
	
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

}
