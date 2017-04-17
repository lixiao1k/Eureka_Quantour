package runner;

import java.rmi.RemoteException;
import java.time.LocalDate;

import exception.StockNameRepeatException;
import logic.service.StockLogicInterface;
import logic.serviceimpl.StockLogicImpl;
import rmi.RemoteHelper;
import vo.SingleStockInfoVO;

/*
 * @author: lxd
 * @time: 2017/3/12
 */
public class ServerRunner {
	public ServerRunner(){
		new RemoteHelper();
	}
	public static void main(String[] args) {
		new ServerRunner();
	}

}
