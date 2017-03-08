package presentation.chart.klineChart;

import vo.SingleStockInfoVO;

import java.util.Calendar;

/**
 *
 * @author RobTerpilowski
 */
public class BarData  {
        


    protected double open;
    protected double high;
    protected double low;
    protected double close;
    protected long volume = 0;
    protected Calendar dateTime;

    public BarData(SingleStockInfoVO stock) {
        this.dateTime=stock.getDate();
        this.open=stock.getOpen();
        this.close=stock.getClose();
        this.low=stock.getLow();
        this.high=stock.getHigh();
        this.volume=stock.getVolume();

    }

    public BarData(Calendar dateTime, double open, double high, double low, double close, long volume) {
        this.dateTime = dateTime;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
        this.volume = volume;
    }


    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the open price of this bar.
     */
    public double getOpen() {
        return open;
    }

    /**
     * @return the High price of this bar.
     */
    public double getHigh() {
        return high;
    }

    /*
     * @return the Low price of this Bar.
     */
    public double getLow() {
        return low;
    }

    /**
     * @return the close price for this bar.
     */
    public double getClose() {
        return close;
    }

    /**
     * @return the Volume for this bar.
     */
    public long getVolume() {
        return volume;
    }

    /**
     * @return the open interest for this bar.
     */


    /**
     * Sets the open price for this bar.
     *
     * @param open The open price for this bar.
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     * Sets the high price for this bar.
     *
     * @param high The high price for this bar.
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     * Sets the low price for this bar.
     *
     * @param low The low price for this bar.
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     * Sets the closing price for this bar.
     *
     * @param close The closing price for this bar.
     */
    public void setClose(double close) {
        this.close = close;
    }

    /**
     * Sets the volume for this bar.
     *
     * @param volume Sets the volume for this bar.
     */
    public void setVolume(long volume) {
        this.volume = volume;
    }
    
    
    /**
     * Updates the last price, adjusting the high and low
     * @param close The last price
     */
    public void update( double close ) {
        if( close > high ) {
            high = close;
        }
        
        if( close < low ) {
            low = close;
        }
        this.close = close;
    }
    





    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(dateTime.getTime());
        sb.append(" Open: ").append(open);
        sb.append(" High: ").append(high);
        sb.append(" Low: ").append(low);
        sb.append(" Close: ").append(close);
        sb.append(" Volume: ").append(volume);
        return sb.toString();
    }//toString()

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(close);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(high);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(low);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(open);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
        result = PRIME * result + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = PRIME * result + (int) (volume ^ (volume >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BarData other = (BarData) obj;
        if (Double.doubleToLongBits(close) != Double.doubleToLongBits(other.close)) {
            return false;
        }
        if (Double.doubleToLongBits(high) != Double.doubleToLongBits(other.high)) {
            return false;
        }
        if (Double.doubleToLongBits(low) != Double.doubleToLongBits(other.low)) {
            return false;
        }
        if (Double.doubleToLongBits(open) != Double.doubleToLongBits(other.open)) {
            return false;
        }

        if (dateTime == null) {
            if (other.dateTime != null) {
                return false;
            }
        } else if (!dateTime.equals(other.dateTime)) {
            return false;
        }
        if (volume != other.volume) {
            return false;
        }
        return true;
    }
    
}
