package com.orford.gapruletest.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.orford.gapruletest.controller.FileDataController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDataControllerTest {
	private static final Logger log = LoggerFactory.getLogger(FileDataControllerTest.class);

	@Autowired
	FileDataController fdController;

	@Test
	public void testControllerLoads() {
		assert(true);
	}


}
