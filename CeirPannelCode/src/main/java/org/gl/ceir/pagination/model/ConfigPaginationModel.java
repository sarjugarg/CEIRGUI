package org.gl.ceir.pagination.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConfigPaginationModel {

	private List<ConfigContentModel> content = null;
	private Pageable pageable;
	private Boolean last;
	private Integer totalPages;
	private Integer totalElements;
	private Boolean first;
	private Sort sort;
	private Integer numberOfElements;
	private Integer size;
	private Integer number;
	private Boolean empty;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public List<ConfigContentModel> getContent() {
		return content;
	}
	public void setContent(List<ConfigContentModel> content) {
		this.content = content;
	}
	public Pageable getPageable() {
		return pageable;
	}
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
	public Boolean getLast() {
		return last;
	}
	public void setLast(Boolean last) {
		this.last = last;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}
	public Boolean getFirst() {
		return first;
	}
	public void setFirst(Boolean first) {
		this.first = first;
	}
	public Sort getSort() {
		return sort;
	}
	public void setSort(Sort sort) {
		this.sort = sort;
	}
	public Integer getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Boolean getEmpty() {
		return empty;
	}
	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		return "ConfigPaginationModel [content=" + content + ", pageable=" + pageable + ", last=" + last
				+ ", totalPages=" + totalPages + ", totalElements=" + totalElements + ", first=" + first + ", sort="
				+ sort + ", numberOfElements=" + numberOfElements + ", size=" + size + ", number=" + number + ", empty="
				+ empty + ", additionalProperties=" + additionalProperties + "]";
	}
	
}
