package com.jobsnearyou.linkedin;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.jobsnearyou.domain.LinkedinJob;

@Service
public class LinkedInJobServiceImpl implements LinkedInJobService {

	@Inject
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<LinkedinJob> findAllLinkedinJobs() {
		Query query = new Query().limit(10);
		return mongoTemplate.find(query, LinkedinJob.class);
	}

	@Override
	public LinkedinJob findOneLinkedInJob(int linkedInJobId) {
		Query query = Query.query(Criteria.where("linkedinJobId").is(linkedInJobId));
		return mongoTemplate.findOne(query, LinkedinJob.class);
	}

	@Override
	public List<LinkedinJob> findAllLinkedInJobsNear(double latitude, double longitude) {
		Query query = Query.query(Criteria.where("location").near(new Point(latitude, longitude))).limit(3);
		return mongoTemplate.find(query, LinkedinJob.class);
	}

	@Override
	public List<LinkedinJob> findAllLinkedInJobsNear(double latitude, double longitude, String skill) {
		Query query = Query.query(Criteria.where("location").near(new Point(latitude, longitude)).and("skills").regex(skill,"i")).limit(3);
		return mongoTemplate.find(query, LinkedinJob.class);
	}

}
