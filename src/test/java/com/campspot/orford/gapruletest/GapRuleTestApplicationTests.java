package com.campspot.orford.gapruletest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.campspot.orford.gapruletest.controller.FileDataController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GapRuleTestApplicationTests {
	private final static Logger log = LoggerFactory.getLogger(GapRuleTestApplicationTests.class);

	@Autowired
	FileDataController fdController;
	
	@Test
	public void assertControllerLoads() {
		log.info("FileDataController is null?: " + (fdController == null));
		assertThat(fdController).isNotNull();
	}

}
