package com.openshift.jobfinder.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.openshift.jobfinder.domain.Job;

@Service
public class JobFinderServiceImpl implements JobFinderService {

	private MongoTemplate mongoTemplate;

	@Inject
	public JobFinderServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<Job> findAllJobs() {
		Query query = new Query().limit(10);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public Job findOneJob(String jobId) {
		Query query = Query.query(Criteria.where("_id").is(jobId));
		return mongoTemplate.findOne(query, Job.class);
	}

	@Override
	public List<Job> findAllJobsNear(double latitude, double longitude) {
		Query query = Query
				.query(Criteria.where("location").near(
						new Point(latitude, longitude))).limit(5);
		return mongoTemplate.find(query, Job.class);
	}

	@Override
	public List<Job> findAllJobsNearWithSkill(double latitude, double longitude,
			String skill) {
		Query query = Query.query(
				Criteria.where("location").near(new Point(latitude, longitude))
						.and("skills").regex(skill, "i")).limit(5);
		return mongoTemplate.find(query, Job.class);
	}

}
