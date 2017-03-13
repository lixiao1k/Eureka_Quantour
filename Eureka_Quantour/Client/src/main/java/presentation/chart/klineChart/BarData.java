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
    
}
