package com.wipro.Trade;

public class Trade {
	private String ticker;
	private double price;
	private int qty;
	private double totalcost;
	
	

	public Trade()
	{
		
	}

	public Trade(String ticker, double price, int qty) {
		super();
		this.ticker = ticker;
		this.price = price;
		this.qty = qty;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(double totalcost) {
		this.totalcost = totalcost;
	}

	
	

}
