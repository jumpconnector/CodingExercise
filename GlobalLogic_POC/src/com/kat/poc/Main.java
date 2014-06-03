package com.kat.poc;

import java.io.FileNotFoundException;

import com.kat.poc.exception.SharePriceReaderException;

/**
 * Main class to Read and display company-wise max share prices from a given csv file
 * Accepts one program argument : filepath to read and process
 * If multiple names are given, only first flle will be considered
 * @author AshwiniK
 *
 */
public class Main {

	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.out.println("Unable to proceed : Please Specify csv file path");
			return;
		}

		SharePriceReader reader = new SharePriceReader(args[0]);
		try {
			reader.displayMaxSharePrices();
		} catch (FileNotFoundException | SharePriceReaderException e) {
			System.out
			.println("Unable to proceed, " + e.getMessage());
		} 
	}
}
