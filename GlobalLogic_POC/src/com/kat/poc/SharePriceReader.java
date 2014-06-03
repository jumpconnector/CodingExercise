package com.kat.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.kat.poc.constants.AppConstants;
import com.kat.poc.exception.SharePriceReaderException;
import com.kat.poc.model.SharePrice;
import com.kat.poc.util.FormatUtil;

/**
 * Reads Max Share prices from a csv file Format of the file needs to be :<br>
 * Year, Month, CompanyA, CompanyB.... CompanyN<br>
 * 2000, Jan, 15, 14.... 17<br>
 * .<br>
 * .<br>
 * 2012, Feb, 14, 16.... 18<br>
 * <br>
 * File can have comment lines starting with '#'<br>
 * 
 * If junk data/value is found in a line it will be discarded while comparing
 * share prices
 * 
 * @author AshwiniK
 * 
 */
public class SharePriceReader {

	private File sharePriceCsvFile; // the file to read for share values
	protected boolean hasJunkData = false; // flag for presence of junk data

	// map to hold share prices keyed on company name
	protected Map<String, SharePrice> sharePriceMap;

	public SharePriceReader(String filePath) {
		if (filePath != null) {
			this.sharePriceCsvFile = new File(filePath);
		}
	}

	/**
	 * Displays max share prices for companies
	 * 
	 * @throws FileNotFoundException
	 * @throws SharePriceReaderException
	 */
	public void displayMaxSharePrices() throws FileNotFoundException,
			SharePriceReaderException {
		/*
		 * Display error if sharePriceCsvFile does not exist
		 */
		if (sharePriceCsvFile == null) {
			throw new SharePriceReaderException("FilePath is null");
		} else if (!sharePriceCsvFile.exists()) {
			throw new SharePriceReaderException("Unable to read file : "
					+ sharePriceCsvFile.getAbsolutePath());
		} else if (sharePriceCsvFile.exists()
				&& sharePriceCsvFile.isDirectory()) {
			throw new SharePriceReaderException(
					"The given filePath refers to a directory");
		}

		if (sharePriceMap == null) {
			readFile();
		}

		if (hasJunkData) {
			System.out
					.println("Junk data found while reading the file, Please check contents");
		}

		if (sharePriceMap == null || sharePriceMap.isEmpty()) {
			throw new SharePriceReaderException("No data to display");
		}

		System.out.println("Displaying max share prices from file : "
				+ sharePriceCsvFile.getAbsolutePath());
		StringBuilder sb = new StringBuilder();
		for (String companyName : sharePriceMap.keySet()) {
			SharePrice sharePrice = sharePriceMap.get(companyName);
			addDisplayableInfo(sharePrice, sb);
		}

		System.out.println(sb.toString());

	}

	/**
	 * Appends sharePriceInfo to string builder for display purposes
	 * 
	 * @param sharePrice
	 * @param sb
	 */
	protected void addDisplayableInfo(SharePrice sharePrice, StringBuilder sb) {
		if (sharePrice == null || sb == null) {
			return;
		}

		FormatUtil.addToStringBuilder(sb, sharePrice.getCompanyName(),
				AppConstants.SPACE, AppConstants.COLON, AppConstants.SPACE,
				sharePrice.getValue(), AppConstants.SPACE, AppConstants.ON_STR,
				AppConstants.SPACE, sharePrice.getMonth(),
				AppConstants.CSV_SEPARATOR, sharePrice.getYear(),
				AppConstants.NEWLINE);
	}

	/**
	 * Populates sharePriceMap with max Share Price for companies
	 * 
	 * @throws FileNotFoundException
	 * @throws SharePriceReaderException
	 */
	void readFile() throws FileNotFoundException, SharePriceReaderException {
		Scanner s = null; // to read file line by line
		try {
			s = new Scanner(new FileInputStream(sharePriceCsvFile));
			String[] headers = null;

			// read till first readable line found for header
			while ((headers = readNextLine(s)) != null) {
				// headers initialized
				if (headers.length > 1) {
					break;
				}
			}

			validateHeader(headers);

			String[] sharePriceInfo = null;
			while ((sharePriceInfo = readNextLine(s)) != null) {

				// empty line or line with junk data
				if (sharePriceInfo.length != headers.length) {
					// set junk data flag and continue
					hasJunkData = true;
					continue;
				}

				String year = sharePriceInfo[0];
				String month = sharePriceInfo[1];

				for (int index = 2; index < sharePriceInfo.length; index++) {
					putInMap(headers[index], year, month, sharePriceInfo[index]);
				}
			}
		} finally {
			// close the scanner
			if (s != null)
				s.close();
		}
	}

	/**
	 * Checks if header has minimum number of companies to proceed
	 * 
	 * @param headers
	 * @return
	 * @throws SharePriceReaderException
	 */
	protected boolean validateHeader(String[] headers)
			throws SharePriceReaderException {
		// display error and return false if no headers
		// or file does not contain at least one company's share values
		if (headers == null) {
			hasJunkData = true;
			throw new SharePriceReaderException(
					"Unable to read headers from file, check format");
		} else if (headers.length < 3) {
			hasJunkData = true;
			throw new SharePriceReaderException(
					"At least one company's data should exist in file");
		}
		return true;
	}

	/**
	 * Tries to put the given info as sharePrice object in sharePriceMap Returns
	 * 'true' if replaces an existing value in map otherwise 'false'
	 * 
	 * @param companyName
	 * @param year
	 * @param month
	 * @param value
	 * @return
	 */
	private boolean putInMap(String companyName, String year, String month,
			String value) {
		if (companyName == null || year == null || month == null
				|| value == null) {
			hasJunkData = true;
			return false;
		}

		Double val = null;

		try {
			val = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			hasJunkData = true;
			return false;
		}

		if (sharePriceMap == null) {
			sharePriceMap = new LinkedHashMap<String, SharePrice>();
		}

		SharePrice sharePrice = sharePriceMap.get(companyName);
		if (sharePrice == null || sharePrice.getValue() == null
				|| val > sharePrice.getValue()) {
			sharePriceMap.put(companyName, new SharePrice(companyName, year,
					month, val));
			return true;
		}

		return false;
	}

	/**
	 * Reads next line from scanner and splits it using formatutil
	 * 
	 * @param s
	 * @return
	 */
	protected String[] readNextLine(Scanner s) {
		if (s.hasNextLine()) {
			String nextLine = s.nextLine();
			if (nextLine.startsWith(AppConstants.HASH)) {
				return readNextLine(s);
			} else {
				return FormatUtil.splitAndFormat(nextLine);
			}
		}
		return null;
	}
}
