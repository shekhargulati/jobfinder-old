package com.jobsnearyou.linkedin;

import java.security.Principal;
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

@Controller
public class LinkedInController {

	@Inject
	private ConnectionRepository connectionRepository;
	@Inject
	private LinkedInJobService linkedInJobService;

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

//	@RequestMapping(value = "/linkedin/jobs", method = RequestMethod.GET)
//	public String jobs(Principal currentUser, Model model) {
//		
//		Connection<LinkedIn> connection = connectionRepository
//				.findPrimaryConnection(LinkedIn.class);
//		if (connection == null) {
//			return "redirect:/connect/linkedin";
//		}
//		LinkedIn api = connection.getApi();
//		LinkedInProfileFull userProfileFull = api.profileOperations().getUserProfileFull();
//		List<String> skills = userProfileFull.getSkills();
//		return null;
//	}
	
	
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
        return new ResponseEntity<String>(LinkedinJob.toJsonArray(jobs),headers, HttpStatus.OK);
	}

	@RequestMapping("/linkedin/jobs/near/{skill}")
	public ResponseEntity<String> allJobsNearWithSkill(@PathVariable("skill")String skill, 
					@RequestParam("latitude")double latitude,@RequestParam("longitude")double longitude) {
		
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
		List<LinkedinJob> jobs = linkedInJobService.findAllLinkedInJobsNear(latitude, longitude,skill);
        return new ResponseEntity<String>(LinkedinJob.toJsonArray(jobs),headers, HttpStatus.OK);
	}
}