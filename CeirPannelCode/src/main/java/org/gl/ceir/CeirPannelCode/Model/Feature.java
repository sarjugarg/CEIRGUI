package org.gl.ceir.CeirPannelCode.Model;
public class Feature {
	private Integer id;
	private String category;
	private String name;
	private String logo;
	private String link;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "Feature [id=" + id + ", category=" + category + ", name=" + name + ", logo=" + logo + ", link=" + link
				+ "]";
	}
}
