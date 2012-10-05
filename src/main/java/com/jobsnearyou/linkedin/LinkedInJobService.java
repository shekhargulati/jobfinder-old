package com.jobsnearyou.linkedin;

import java.util.List;

import com.jobsnearyou.domain.LinkedinJob;

public interface LinkedInJobService {
	List<LinkedinJob> findAllLinkedinJobs();

	LinkedinJob findOneLinkedInJob(int linkedInId);

	List<LinkedinJob> findAllLinkedInJobsNear(double latitude, double longitude);

	List<LinkedinJob> findAllLinkedInJobsNear(double latitude, double longitude, String name);
}
