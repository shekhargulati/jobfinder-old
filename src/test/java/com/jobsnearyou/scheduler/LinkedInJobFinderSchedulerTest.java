package com.jobsnearyou.scheduler;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jobsnearyou.config.MainConfig;
import com.jobsnearyou.config.SecurityConfig;
import com.jobsnearyou.config.SocialConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MainConfig.class, SocialConfig.class,
		SecurityConfig.class })
@ActiveProfiles("simple")
public class LinkedInJobFinderSchedulerTest {

	@Inject
	LinkedInJobFinderScheduler linkedInJobFinderScheduler;

	@Test
	public void testFindJobs() throws Exception{
		linkedInJobFinderScheduler.findJobs();
	}

}
