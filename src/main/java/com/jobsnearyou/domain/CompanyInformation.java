package com.jobsnearyou.domain;

import org.springframework.social.linkedin.api.Company;

public class CompanyInformation {

	private int companyId;
	private String companyName;
	
	
	public CompanyInformation() {
		// TODO Auto-generated constructor stub
	}
	
	public CompanyInformation(Company company){
		this.companyId = company.getId();
		this.companyName = company.getName();
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "CompanyInformation [companyName=" + companyName + "]";
	}
	
}
