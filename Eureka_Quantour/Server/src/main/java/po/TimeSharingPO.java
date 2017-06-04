package po;

import java.util.List;

public class TimeSharingPO {
	public List<Double> getMinute_data() {
		return minute_data;
	}
	public void setMinute_data(List<Double> minute_data) {
		this.minute_data = minute_data;
	}
	public double getLast_close() {
		return last_close;
	}
	public void setLast_close(double last_close) {
		this.last_close = last_close;
	}
	public TimeSharingPO(List<Double> minute_data, double last_close) {
		super();
		this.minute_data = minute_data;
		this.last_close = last_close;
	}
	private List<Double> minute_data;
	private double last_close;
	
}
