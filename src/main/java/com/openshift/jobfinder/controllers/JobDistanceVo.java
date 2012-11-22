package com.openshift.jobfinder.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.openshift.jobfinder.domain.Job;
import com.openshift.jobfinder.googleapis.Distance;
import com.openshift.jobfinder.googleapis.Duration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JobDistanceVo {

	private Job job;

	private Distance distance;

	private Duration duration;

	public JobDistanceVo() {
		// TODO Auto-generated constructor stub
	}

	public JobDistanceVo(Job job, Distance distance, Duration duration) {
		this.job = job;
		this.distance = distance;
		this.duration = duration;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
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
		return new JSONSerializer().include("job").include("distance")
				.include("duration").exclude("*.class").serialize(this);
	}

	public static JobDistanceVo fromJson(String json) {
		return new JSONDeserializer<JobDistanceVo>()
				.use(null, JobDistanceVo.class).use("job", Job.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

	public static String toJsonArray(Collection<JobDistanceVo> collection) {
		return new JSONSerializer().include("job").include("distance")
				.include("duration").exclude("*.class").serialize(collection);
	}

	public static Collection<JobDistanceVo> fromJsonArray(String json) {
		return new JSONDeserializer<List<JobDistanceVo>>()
				.use(null, ArrayList.class).use("job", Job.class)
				.use("distance", Distance.class)
				.use("duration", Duration.class).deserialize(json);
	}

}
