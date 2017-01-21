package kh.com.kshrd.v3;

import java.util.List;


public class SectionDescription {
	private int id;
	private String section;
	private String title;
	private String number;
	private String page;
	private List<Description> description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Description> getDescription() {
		return description;
	}
	public void setDescription(List<Description> description) {
		this.description = description;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "SectionDescription [id=" + id + ", section=" + section + ", title=" + title + ", number=" + number
				+ ", page=" + page + ", description=" + description + "]";
	}
	
	
}
