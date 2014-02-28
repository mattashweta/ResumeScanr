package com.SCYYahooligens.android.extractor;

public class InfoExtractor {
	private Resume instance;
	
	public InfoExtractor()
	{
		instance = new Resume();
	}
	
	public Resume getInfo(String s)
	{
		instance.studName = extractName();
		instance.studGPA = extractGPA();
		instance.studEmail = extractEmail();
		instance.studBranch = extractBranch();
		instance.studUniv = extractUniv();
		return instance;
	}
	private String extractName()
	{
		return null;
	}
	private String extractGPA()
	{
		return null;
	}
	private String extractEmail()
	{
		return null;
	}
	private String extractBranch()
	{
		return null;
	}
	private String extractUniv()
	{
		return null;
	}
}
