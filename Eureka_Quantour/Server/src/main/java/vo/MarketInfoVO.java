package vo;

import java.io.Serializable;

/**
 * 
 * @Description: gather the data of market in one day
 * @author: hzp
 * @time: 2017年3月6日
 */
public class MarketInfoVO implements Serializable{
	
	private static final long serialVersionUID = -8022819757439228511L;

	/**
	 * EFP: exceed five percent
	 * LTFP: less than five percent
	 * OMC: open minus close
	 *@param: volume: the total trade of stock today
	 *@param: numOfRiseStop: the number of stock which limits up today
	 *@param: numOfDropStop: the number of stock which limits down today
	 *@param: numOfRiseEFP: the number of stock which rises EFP
	 *@param: numOfDropEFP: the number of stock which drops EFP
	 *@param: numOfOMCEFP: the number of stock whose OMC EFP
	 *@param: numOfOMCLTFP: the number of stock whose OMC LTFP
	 */
	private int volume = 0;
	private int numOfRiseStop = 0;
	private int numOfDropStop = 0;
	private int numOfRiseEFP =0;
	private int numOfDropEFP =0;
	private int numOfOMCEFP = 0;
	private int numOfOMCLTFP = 0;
	
	public int getVolume(){
		return this.volume;
	}
	public int getNumOfRiseStop(){
		return this.numOfRiseStop;
	}
	public int getNumOfDropStop(){
		return this.numOfDropStop;
	}
	public int getNumOfRiseEFP(){
		return this.numOfRiseEFP;
	}
	public int getNumOfDropEFP(){
		return this.numOfDropEFP;
	}
	public int getNumOfOMCEFP(){
		return this.numOfOMCEFP;
	}
	public int getNumOfOMCLTFP(){
		return this.numOfOMCLTFP;
	}
	
	public void setVolume(int volume){
		this.volume = volume;
	}
	public void setNumOfRiseStop(int numOfRiseStop){
		this.numOfRiseStop = numOfRiseStop;
	}
	public void setNumOfDropStop(int numOfDropStop){
		this.numOfDropStop = numOfDropStop;
	}
	public void setNumOfRiseEFP(int numOfRiseEFP){
		this.numOfRiseEFP = numOfRiseEFP;
	}
	public void setNumOfDropEFP(int numOfDropEFP){
		this.numOfDropEFP = numOfDropEFP;
	}
	public void setNumOfOMCEFP(int numOfOMCEFP){
		this.numOfOMCEFP = numOfOMCEFP;
	}
	public void setNumOfOMCLTFP(int numOfOMCLTFP){
		this.numOfOMCLTFP = numOfOMCLTFP;
	}
	
	public MarketInfoVO initObject(int volume, int numOfRiseStop, int numOfDropStop, 
			int numOfRiseEFP, int numOfDropEFP, int numOfOMCEFP, int numOfOMCLTFP){
		MarketInfoVO mi = new MarketInfoVO();
		mi.setVolume(volume); 
		mi.setNumOfRiseStop(numOfRiseStop); mi.setNumOfDropStop(numOfDropStop);
		mi.setNumOfRiseEFP(numOfRiseEFP); mi.setNumOfDropEFP(numOfDropEFP);
		mi.setNumOfOMCEFP(numOfOMCEFP); mi.setNumOfOMCLTFP(numOfOMCLTFP);
		return mi;
	}
}
