package org.gl.ceir.graph.model;

import java.util.List;

import org.gl.ceir.pagination.model.Pageable;
import org.springframework.stereotype.Component;
@Component
public class BlockIMEIContent {
	private BlockIMEIGraph content;
	private Pageable pageable;
	private Integer totalPages;
	private Integer totalElements;
	private Boolean last;
	private Boolean first;
	private Integer numberOfElements;
	private Integer size;
	private Integer number;
	private Boolean empty;
	public BlockIMEIGraph getContent() {
		return content;
	}
	public void setContent(BlockIMEIGraph content) {
		this.content = content;
	}
	public Pageable getPageable() {
		return pageable;
	}
	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
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
	public Boolean getLast() {
		return last;
	}
	public void setLast(Boolean last) {
		this.last = last;
	}
	public Boolean getFirst() {
		return first;
	}
	public void setFirst(Boolean first) {
		this.first = first;
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockIMEIContent [content=");
		builder.append(content);
		builder.append(", pageable=");
		builder.append(pageable);
		builder.append(", totalPages=");
		builder.append(totalPages);
		builder.append(", totalElements=");
		builder.append(totalElements);
		builder.append(", last=");
		builder.append(last);
		builder.append(", first=");
		builder.append(first);
		builder.append(", numberOfElements=");
		builder.append(numberOfElements);
		builder.append(", size=");
		builder.append(size);
		builder.append(", number=");
		builder.append(number);
		builder.append(", empty=");
		builder.append(empty);
		builder.append("]");
		return builder.toString();
	}
	

}
