package vn.com.omart.sharedkernel.application.model.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageDTO<T> implements Serializable {

	private static final long serialVersionUID = -8848790648729192679L;

	private boolean last;
	private boolean first;
	private Long totalElements;
	private int totalPages;
	private int size;
	private int number;
	private int numberOfElements;
	List<T> content;

	public void setPageDTO(Page page) {
		this.setFirst(page.isFirst());
		this.setLast(page.isLast());
		this.setTotalElements(page.getTotalElements());
		this.setTotalPages(page.getTotalPages());
		this.setSize(page.getSize());
		this.setNumber(page.getNumber());
		this.setNumberOfElements(page.getNumberOfElements());
	}
	
	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
