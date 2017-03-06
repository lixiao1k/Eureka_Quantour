## 对象

* singleStockInfo : date , code , open , close , high , low , volume
* comparedInfo : A and B's processed info
* marketInfo : volume 、 涨（跌）停股票数、涨（跌）幅超过
   5% 的股票数、开盘‐收盘大（小）于 5%*上一个交易日收盘价的
   股票个数

## 界面层需（逻辑层）接口/逻辑层供（界面层）接口

* 注册 public boolean signUp ( String username, String password )
* 登录 public boolean signIn ( String username, String password )
* 搜索 public Iterator< singleStockInfo > getSingleStockInfoByTime ( String stockCode, Calendar begin, Calendar end )
* public Iterator< double > getEMAInfo ( String stockCode, Calendar begin, Calendar end, int method )
* public comparedInfo  getComparedInfo ( String stockCodeA, String stockCodeB, Calendar begin, Calendar end )
* public marketInfo getMarketInfo ( Calendar date )

## 逻辑层需（数据层）接口/数据层供（逻辑层）接口

* public boolean signUpCheck ( String username, String password )


* public boolean signInCheck ( String username, String password )
* public List< singleStockInfo > getSingleStockInfo  ( String stockCode, Calendar begin, Calendar end )
* public List< singleStockInfo > getMarketByDate ( Calendar date )

## 逻辑层自己的方法

* public comparedInfo  compare ( List< singleStockInfo > ,  List< singleStockInfo >)

## 数据层自己的方法

* public boolean insert ( String username, String password )