package com.kat.poc.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kat.poc.util.FormatUtil;

public class FormatUtilTest {

	@Test
	public void test_splitAndFormat() {
		System.out.println("Test splitAndFormat...");
		String[] retArr = FormatUtil.splitAndFormat(" A , B, D , C");
		assertNotNull(retArr);
		assertEquals(retArr.length, 4);
		assertEquals(retArr[0], "A");
		assertEquals(retArr[1], "B");
		assertEquals(retArr[2], "D");
		assertEquals(retArr[3], "C");
	}

}
