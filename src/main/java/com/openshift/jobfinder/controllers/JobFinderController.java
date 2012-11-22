package com.openshift.jobfinder.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.openshift.jobfinder.domain.Account;
import com.openshift.jobfinder.domain.Job;
import com.openshift.jobfinder.googleapis.DistanceResponse;
import com.openshift.jobfinder.googleapis.GoogleDistanceClient;
import com.openshift.jobfinder.jdbc.repository.AccountRepository;
import com.openshift.jobfinder.service.CoordinateFinder;
import com.openshift.jobfinder.service.JobFinderService;
import com.openshift.jobfinder.utils.SecurityUtils;

@Controller
public class JobFinderController {

	@Inject
	private JobFinderService jobFinderService;

	@Inject
	private GoogleDistanceClient googleDistanceClient;

	@Inject
	private CoordinateFinder coordinateFinder;

	@Inject
	private AccountRepository accountRepository;

	@RequestMapping("/jobs")
	public ResponseEntity<String> allJobs() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Job> jobs = jobFinderService.findAllJobs();
		return new ResponseEntity<String>(Job.toJsonArray(jobs), headers,
				HttpStatus.OK);
	}

	@RequestMapping("/jobs/{jobId}")
	public ResponseEntity<String> oneJob(@PathVariable("jobId") String jobId) {
		Job job = jobFinderService.findOneJob(jobId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (job == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(job.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.POST)
	public ResponseEntity<String> createNewJob(@Valid Job job) {
		jobFinderService.saveJob(job);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(job.toJson(), headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/jobs/{jobId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteJob(@PathVariable("jobId")String jobId){
		Job job = jobFinderService.findOneJob(jobId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (job == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		jobFinderService.deleteJob(job);
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping("/jobs/near")
	public String allJobsNearToLatitudeAndLongitude(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Job> jobs = jobFinderService.findAllJobsNear(latitude, longitude);
		List<JobDistanceVo> localJobsWithDistance = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			localJobsWithDistance.add(linkedinJobWithDistance);
		}

		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	@RequestMapping("/jobs/near/{skill}")
	public String allJobsNearLatitideAndLongitudeWithSkill(
			@PathVariable("skill") String skill,
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude, Model model) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<JobDistanceVo> localJobsWithDistance = findJobs(skill, latitude,
				longitude);
		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	@RequestMapping("/jobs/near/{location}/{skill}")
	public String allJobsNearToLocationWithSkill(
			@PathVariable("location") String location,
			@PathVariable("skill") String skill, Model model) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		double[] coordinates = coordinateFinder.find(location);
		if (ArrayUtils.isEmpty(coordinates)) {
			return "redirect:/linkedin";
		}

		double latitude = coordinates[0];
		double longitude = coordinates[1];
		List<JobDistanceVo> localJobsWithDistance = findJobs(skill, latitude,
				longitude);
		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	@RequestMapping("/jobsforme")
	public String allJobsForMe(Model model) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Account account = accountRepository.findAccountByUsername(SecurityUtils
				.getCurrentLoggedInUsername());
		double[] coordinates = coordinateFinder.find(account.getAddress());
		if (ArrayUtils.isEmpty(coordinates)) {
			return "redirect:/linkedin";
		}

		double latitude = coordinates[0];
		double longitude = coordinates[1];
		List<JobDistanceVo> localJobsWithDistance = findJobsWithLocation(
				latitude, longitude);
		model.addAttribute("jobs", localJobsWithDistance);
		return "jobs";
	}

	private List<JobDistanceVo> findJobs(String skill, double latitude,
			double longitude) {
		List<Job> jobs = jobFinderService.findAllJobsNearWithSkill(latitude,
				longitude, skill);
		List<JobDistanceVo> locaJobsWithDistance = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			locaJobsWithDistance.add(linkedinJobWithDistance);
		}
		return locaJobsWithDistance;
	}

	private List<JobDistanceVo> findJobsWithLocation(double latitude,
			double longitude) {
		List<Job> jobs = jobFinderService.findAllJobsNear(latitude, longitude);
		List<JobDistanceVo> locaJobsWithDistance = new ArrayList<JobDistanceVo>();
		for (Job localJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(
					localJob.getLocation(),
					new double[] { latitude, longitude });
			JobDistanceVo linkedinJobWithDistance = new JobDistanceVo(localJob,
					response.rows[0].elements[0].distance,
					response.rows[0].elements[0].duration);
			locaJobsWithDistance.add(linkedinJobWithDistance);
		}
		return locaJobsWithDistance;
	}
}