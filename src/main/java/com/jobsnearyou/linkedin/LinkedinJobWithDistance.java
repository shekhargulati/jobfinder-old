package com.jobsnearyou.linkedin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jobsnearyou.domain.LinkedinJob;
import com.jobsnearyou.googleapis.Distance;
import com.jobsnearyou.googleapis.Duration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LinkedinJobWithDistance {

	private LinkedinJob linkedinJob;

	private Distance distance;

	private Duration duration;

	public LinkedinJobWithDistance() {
		// TODO Auto-generated constructor stub
	}

	public LinkedinJobWithDistance(LinkedinJob linkedinJob, Distance distance,
			Duration duration) {
		this.linkedinJob = linkedinJob;
		this.distance = distance;
		this.duration = duration;
	}

	public LinkedinJob getLinkedinJob() {
		return linkedinJob;
	}

	public void setLinkedinJob(LinkedinJob linkedinJob) {
		this.linkedinJob = linkedinJob;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String toJson() {
		return new JSONSerializer().include("linkedinJob").include("distance")
				.include("duration").exclude("*.class").serialize(this);
	}

	public static LinkedinJobWithDistance fromJson(String json) {
		return new JSONDeserializer<LinkedinJobWithDistance>()
				.use(null, LinkedinJobWithDistance.class)
				.use("linkedinJob", LinkedinJob.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

	public static String toJsonArray(
			Collection<LinkedinJobWithDistance> collection) {
		return new JSONSerializer().include("linkedinJob").include("distance")
				.include("duration").exclude("*.class").serialize(collection);
	}

	public static Collection<LinkedinJobWithDistance> fromJsonArray(String json) {
		return new JSONDeserializer<List<LinkedinJobWithDistance>>()
				.use(null, ArrayList.class)
				.use("linkedinJob", LinkedinJob.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

}
