package data.datahelperservice;

import java.time.LocalDate;
import java.util.List;

import po.SingleStockInfoPO;

public interface IExponentDataHelper {
	public List<SingleStockInfoPO> getPeriodExponent(String name,LocalDate start,LocalDate end);
	public LocalDate getExponentMaxDay(String name);
	public LocalDate getExponentMinDay(String name);
}
