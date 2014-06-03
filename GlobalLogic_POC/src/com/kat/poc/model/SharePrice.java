package com.kat.poc.model;

/**
 * Class to hold SharePrice Information
 * 
 * @author AshwiniK
 * 
 */
public class SharePrice {
	private String companyName;
	private String year;
	private String month;
	private Double value;

	public SharePrice(String companyName, String year, String month,
			Double value) {
		this.companyName = companyName;
		this.year = year;
		this.month = month;
		this.value = value;
	}

	/**
	 * Returns the Company's name
	 * 
	 * @return
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Returns the Year
	 * 
	 * @return
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Returns the Month
	 * 
	 * @return
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Returns the share value
	 * 
	 * @return
	 */
	public Double getValue() {
		return value;
	}
}