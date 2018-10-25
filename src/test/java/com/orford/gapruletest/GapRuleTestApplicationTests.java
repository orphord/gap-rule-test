package com.orford.gapruletest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.orford.gapruletest.controller.GapRuleController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GapRuleTestApplicationTests {
	private final static Logger log = LoggerFactory.getLogger(GapRuleTestApplicationTests.class);

	@Autowired
	GapRuleController fdController;
	
	@Test
	public void assertControllerLoads() {
		log.info("GapRuleController is null?: " + (fdController == null));
		assertThat(fdController).isNotNull();
	}

}
