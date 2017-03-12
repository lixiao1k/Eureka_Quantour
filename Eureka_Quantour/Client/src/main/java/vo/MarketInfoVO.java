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
	private int numOfRiseLTFP =0;
	private int numOfDropLTFP =0;
	private int numOfOMCLTFP = 0;
	private int numOfOMCSTFP = 0;
	
	public int getVolume(){
		return this.volume;
	}
	public int getNumOfRiseStop(){
		return this.numOfRiseStop;
	}
	public int getNumOfDropStop(){
		return this.numOfDropStop;
	}
	public int getNumOfRiseLTFP(){
		return this.numOfRiseLTFP;
	}
	public int getNumOfDropLTFP(){
		return this.numOfDropLTFP;
	}
	public int getNumOfOMCLTFP(){
		return this.numOfOMCLTFP;
	}
	public int getNumOfOMCSTFP(){
		return this.numOfOMCSTFP;
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
	public void setNumOfRiseLTFP(int numOfRiseLTFP){
		this.numOfRiseLTFP = numOfRiseLTFP;
	}
	public void setNumOfDropLTFP(int numOfDropLTFP){
		this.numOfDropLTFP = numOfDropLTFP;
	}
	public void setNumOfOMCLTFP(int numOfOMCLTFP){
		this.numOfOMCLTFP = numOfOMCLTFP;
	}
	public void setNumOfOMCSTFP(int numOfOMCSTFP){
		this.numOfOMCSTFP = numOfOMCSTFP;
	}
	
	public MarketInfoVO initObject(int volume, int numOfRiseStop, int numOfDropStop, int numOfRiseLTFP,
			int numOfDropLTFP, int numOfOMCLTFP, int numOfOMCSTFP){
		MarketInfoVO mi = new MarketInfoVO();
		mi.setVolume(volume); mi.setNumOfRiseStop(numOfRiseStop); mi.setNumOfDropStop(numOfDropStop);
		mi.setNumOfRiseLTFP(numOfRiseLTFP); mi.setNumOfDropLTFP(numOfDropLTFP);
		mi.setNumOfOMCLTFP(numOfOMCLTFP); mi.setNumOfOMCSTFP(numOfOMCSTFP);
		return mi;
	}
}
