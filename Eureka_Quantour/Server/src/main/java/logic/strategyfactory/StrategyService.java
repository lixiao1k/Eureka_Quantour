package logic.strategyfactory;

import exception.DateOverException;
import exception.NullStockIDException;

public interface StrategyService {
	public void calculate() throws NullStockIDException;
	public double getZheCi();
	public double getXiaCi();
	public boolean isContinue();
}
