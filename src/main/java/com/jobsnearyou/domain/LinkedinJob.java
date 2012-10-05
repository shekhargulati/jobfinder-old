package com.jobsnearyou.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.social.linkedin.api.Job;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Document(collection = "jobs")
public class LinkedinJob {

	@Id
	private String id;

	private int linkedinJobId;

	private CompanyInformation company;

	private String description;

	private String locationDescription;

	private String city;

	private String country;

	private String state;

	private boolean active;

	private Date expirationDate;

	private String jobSiteUrl;

	private String jobTitle;

	private double[] location;

	private String[] skills;

	private String formattedAddress;

	public LinkedinJob() {

	}

	public LinkedinJob(Job job, String skill) {
		this.linkedinJobId = job.getId();
		this.description = job.getDescription();
		this.locationDescription = job.getLocationDescription();
		this.city = job.getPosition().getLocation().getName();
		this.country = job.getPosition().getLocation().getCountry();
		this.active = job.isActive();
		this.expirationDate = job.getExpirationTimestamp();
		this.jobSiteUrl = job.getSiteJobUrl();
		this.company = new CompanyInformation(job.getCompany());
		this.jobTitle = job.getPosition().getTitle();
		this.skills = findSkills(job.getDescription(), skill);
	}

	private String[] findSkills(String fullJobDescription, String skill) {
		String str = "<br><br>SKILL REQUIREMENTS<br><br>";
		int startIndex = fullJobDescription.indexOf(str);
		if (startIndex == -1) {
			return new String[] { skill };
		}
		String str1 = StringUtils.remove(
				fullJobDescription.substring(startIndex), str);
		int indexOf = str1.indexOf("<br><br>");
		String toRemove = str1.substring(indexOf);
		String[] jobSkills = StringUtils.remove(str1, toRemove).split(",");
		return cleanse(jobSkills);
	}

	private String[] cleanse(String[] jobSkills) {
		List<String> allSkills = new ArrayList<String>();
		for (String jobSkill : jobSkills) {
			allSkills.add(StringUtils.trim(jobSkill).toLowerCase());
		}
		return allSkills.toArray(new String[0]);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLinkedinJobId() {
		return linkedinJobId;
	}

	public void setLinkedinJobId(int linkedinJobId) {
		this.linkedinJobId = linkedinJobId;
	}

	public CompanyInformation getCompany() {
		return company;
	}

	public void setCompany(CompanyInformation company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getJobSiteUrl() {
		return jobSiteUrl;
	}

	public void setJobSiteUrl(String jobSiteUrl) {
		this.jobSiteUrl = jobSiteUrl;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public double[] getLocation() {
		return location;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	@Override
	public String toString() {
		return "LinkedinJob [linkedinJobId=" + linkedinJobId + ", company="
				+ company + ", locationDescription=" + locationDescription
				+ ", city=" + city + ", country=" + country + ", jobTitle="
				+ jobTitle + ", location=" + Arrays.toString(location) + "]";
	}

	public String toJson() {
		return new JSONSerializer().include("location").include("company").include("skills").exclude("*.class")
				.serialize(this);
	}

	public static LinkedinJob fromJson(String json) {
		return new JSONDeserializer<LinkedinJob>().use(null, LinkedinJob.class)
				.deserialize(json);
	}

	public static String toJsonArray(Collection<LinkedinJob> collection) {
		return new JSONSerializer().include("location").include("company").include("skills").exclude("*.class")
				.serialize(collection);
	}

	public static Collection<LinkedinJob> fromJsonArray(String json) {
		return new JSONDeserializer<List<LinkedinJob>>()
				.use(null, ArrayList.class).use("location", LinkedinJob.class)
				.deserialize(json);
	}

}
