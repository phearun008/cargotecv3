package kh.com.kshrd.v3;

public class SectionImage {
	private int id;
	private String section;
	private String title;
	private String number;
	private String url;
	private String page;
	
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "SectionImage [id=" + id + ", section=" + section + ", title=" + title + ", number=" + number + ", url="
				+ url + ", page=" + page + "]";
	}
}
