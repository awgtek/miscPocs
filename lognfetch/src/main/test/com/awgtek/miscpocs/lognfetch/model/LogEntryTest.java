package com.awgtek.miscpocs.lognfetch.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LogEntryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetParameter() {
		LogEntry logEntry = new LogEntry();
		logEntry.setParameter("attribute1", "attribute 1");
		logEntry.setParameter("num1", "22");
		logEntry.setParameter("num2", "555");
		logEntry.setParameter("category", "CAT1");
		assertEquals(LogEntry.LogCategory.CAT1, logEntry.getCategory());
	}

}
