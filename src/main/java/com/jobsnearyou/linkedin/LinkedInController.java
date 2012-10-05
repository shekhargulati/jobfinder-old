package com.jobsnearyou.linkedin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobsnearyou.domain.LinkedinJob;
import com.jobsnearyou.googleapis.DistanceResponse;
import com.jobsnearyou.googleapis.GoogleDistanceClient;

@Controller
public class LinkedInController {

	@Inject
	private ConnectionRepository connectionRepository;
	@Inject
	private LinkedInJobService linkedInJobService;
	@Inject
	private GoogleDistanceClient googleDistanceClient;

	@RequestMapping(value = "/linkedin", method = RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		Connection<LinkedIn> connection = connectionRepository
				.findPrimaryConnection(LinkedIn.class);
		if (connection == null) {
			return "redirect:/connect/linkedin";
		}
		LinkedInProfile profile = connection.getApi().profileOperations()
				.getUserProfile();
		model.addAttribute("profile", profile);
		return "linkedin/profile";
	}

	@RequestMapping("/linkedin/jobs")
	public ResponseEntity<String> allJobs() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
		List<LinkedinJob> jobs = linkedInJobService.findAllLinkedinJobs();
		return new ResponseEntity<String>(LinkedinJob.toJsonArray(jobs),headers, HttpStatus.OK);
	}

	@RequestMapping("/linkedin/jobs/{jobId}")
	public ResponseEntity<String> oneJob(@PathVariable("jobId") int jobId) {
		LinkedinJob job = linkedInJobService.findOneLinkedInJob(jobId);
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (job == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(job.toJson(), headers,
                HttpStatus.OK);
	}

	@RequestMapping("/linkedin/jobs/near")
	public ResponseEntity<String> allJobsNear(@RequestParam("latitude")double latitude, 
											   @RequestParam("longitude")double longitude) {

		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
		List<LinkedinJob> jobs = linkedInJobService.findAllLinkedInJobsNear(latitude, longitude);
		List<LinkedinJobWithDistance> linkedinJobWithDistances = new ArrayList<LinkedinJobWithDistance>();
		for (LinkedinJob linkedinJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(linkedinJob.getLocation(), new double[]{latitude,longitude});
			LinkedinJobWithDistance linkedinJobWithDistance = new LinkedinJobWithDistance(linkedinJob,response.rows[0].elements[0].distance,response.rows[0].elements[0].duration);
			linkedinJobWithDistances.add(linkedinJobWithDistance);
		}
		
        return new ResponseEntity<String>(LinkedinJobWithDistance.toJsonArray(linkedinJobWithDistances),headers, HttpStatus.OK);
	}
	
	@RequestMapping("/linkedin/jobs/near/{skill}")
	public ResponseEntity<String> allJobsNearWithSkill(@PathVariable("skill")String skill, 
					@RequestParam("latitude")double latitude,@RequestParam("longitude")double longitude) {
		
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
		List<LinkedinJob> jobs = linkedInJobService.findAllLinkedInJobsNear(latitude, longitude,skill);
		List<LinkedinJobWithDistance> linkedinJobWithDistances = new ArrayList<LinkedinJobWithDistance>();
		for (LinkedinJob linkedinJob : jobs) {
			DistanceResponse response = googleDistanceClient.findDirections(linkedinJob.getLocation(), new double[]{latitude,longitude});
			LinkedinJobWithDistance linkedinJobWithDistance = new LinkedinJobWithDistance(linkedinJob,response.rows[0].elements[0].distance,response.rows[0].elements[0].duration);
			linkedinJobWithDistances.add(linkedinJobWithDistance);
		}
        return new ResponseEntity<String>(LinkedinJobWithDistance.toJsonArray(linkedinJobWithDistances),headers, HttpStatus.OK);
	}
}