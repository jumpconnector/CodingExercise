package com.kat.poc;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.kat.poc.constants.AppConstants;
import com.kat.poc.exception.SharePriceReaderException;
import com.kat.poc.model.SharePrice;

public class SharePriceReaderTest {

	SharePriceReader reader = new SharePriceReader("files/test.csv");
	
	/**
	 * Tests if display message is correctly formed
	 */
	@Test
	public void test_addDisplayableInfo() {
		SharePrice price = new SharePrice("a", "2000", "Jan", 53.05);
		StringBuilder sb1 = new StringBuilder();
		reader.addDisplayableInfo(price, sb1);
		StringBuilder sb2 = new StringBuilder();
		
		sb2.append(price.getCompanyName());
		sb2.append(AppConstants.SPACE);
		sb2.append(AppConstants.COLON);
		sb2.append(AppConstants.SPACE);
		sb2.append(price.getValue());
		sb2.append(AppConstants.SPACE);
		sb2.append(AppConstants.ON_STR);
		sb2.append(AppConstants.SPACE);
		sb2.append(price.getMonth());
		sb2.append(AppConstants.CSV_SEPARATOR);
		sb2.append(price.getYear());
		sb2.append(AppConstants.NEWLINE);
		
		assertEquals(sb1.toString(), sb2.toString());
	}
	
	/**
	 * Tests method which validates header values in file
	 */
	@Test
	public void test_isValidHeader() {
		String[] invalidHeader1 = {""};
		String[] invalidHeader2 = {"", ""};
		String[] invalidHeader3 = null;
		String[] validHeader = {"", "", ""};
		
		try {
			assertEquals(false, reader.validateHeader(invalidHeader1));
			fail("Valid Header check failed, SharePriceReaderException not thrown");
		} catch (SharePriceReaderException e) {
			assertNotNull(e);
		}
		
		try {
			assertEquals(false, reader.validateHeader(invalidHeader2));
			fail("Valid Header check failed, SharePriceReaderException not thrown");
		} catch (SharePriceReaderException e) {
			assertNotNull(e);
		}
		
		try {
			assertEquals(false, reader.validateHeader(invalidHeader3));
			fail("Valid Header check failed, SharePriceReaderException not thrown");
		} catch (SharePriceReaderException e) {
			assertNotNull(e);
		}
		
		try {
			assertEquals(true, reader.validateHeader(validHeader));
		} catch (SharePriceReaderException e) {
			fail("Valid Header check failed, SharePriceReaderException thrown");
		}
		
		SharePriceReader reader = new SharePriceReader("files/noHeader.csv");
		try {
			reader.readFile();
			fail("SharePriceReaderException not thrown");
		} catch (FileNotFoundException e){
			fail("FileNotFoundException thrown");
		} catch (SharePriceReaderException e) {
			assertNotNull(e);
		}
	}
	
	/**
	 * Tests if correctly throws FileNotFoundException
	 */
	@Test
	public void test_fileNotFoundException() {
		SharePriceReader reader = new SharePriceReader("aFileThatDoesNotExist");
		try {
			reader.readFile();
			fail("FileNotFoundException not thrown");
		} catch (FileNotFoundException e) {
			assertNotNull(e);
		} catch (SharePriceReaderException e) {
			fail("SharePriceReaderException thrown");
		}
	}
	
	/**
	 * Tests if a file is properly read
	 */
	@Test
	public void test_fileProperRead() {
		try {
			reader.readFile();
			assertEquals(reader.hasJunkData, false);
		} catch (FileNotFoundException |SharePriceReaderException e) {
			fail("Excepton : " + e.getMessage());
		}
	}
	
	/**
	 * TEsts if junk data is correctly detected
	 */
	@Test
	public void test_junkDataDetection() {
		SharePriceReader reader = new SharePriceReader("files/junkData.csv");
		try {
			reader.readFile();
			assertEquals(reader.hasJunkData, true);
		} catch (FileNotFoundException |SharePriceReaderException e) {
			fail("Excepton : " + e.getMessage());
		}
	}
	
	/**
	 * Checks if correct max share values/year/month/companyName are read and stored
	 */
	@Test
	public void test_correctMaxValue() {
		SharePriceReader reader = new SharePriceReader("files/maxTest.csv");
		try {
			reader.readFile();
			assertNotNull(reader.sharePriceMap);
			SharePrice forA = reader.sharePriceMap.get("A");
			SharePrice forB = reader.sharePriceMap.get("B");
			SharePrice forC = reader.sharePriceMap.get("C");
			
			assertNotNull(forA);
			assertNotNull(forB);
			assertNotNull(forC);
			
			assertEquals(forA.getValue(), 14.0, 0);
			assertEquals(forB.getValue(), 18.0, 0);
			assertEquals(forC.getValue(), 22.0, 0);
			
			assertEquals(forA.getCompanyName(), "A");
			assertEquals(forB.getCompanyName(), "B");
			assertEquals(forC.getCompanyName(), "C");
			
			assertEquals(forA.getYear(), "1992");
			assertEquals(forB.getYear(), "1991");
			assertEquals(forC.getYear(), "1990");
			
			assertEquals(forA.getMonth(), "Dec");
			assertEquals(forB.getMonth(), "Mar");
			assertEquals(forC.getMonth(), "Jan");
		} catch (FileNotFoundException |SharePriceReaderException e) {
			fail("Excepton : " + e.getMessage());
		}
	}
}
