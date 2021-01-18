/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yq.xcode.common.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Basic {@code Page} implementation.
 * 
 * @param <T> the type of which the page consists.
 * @author Oliver Gierke
 */
public class YqPageImpl<T>  implements Page<T> {

	private static final long serialVersionUID = 867755909294344406L;

	private final List<T> content = new ArrayList<T>();

    @JsonIgnore
	private final Pageable pageable;
	private final long total;

	/**
	 * Constructor of {@code PageImpl}.
	 * 
	 * @param content the content of this page, must not be {@literal null}.
	 * @param pageable the paging information, can be {@literal null}.
	 * @param total the total amount of items available
	 */
	public YqPageImpl(List<T> content, Pageable pageable, long total) {

		if (null == content) {
			throw new IllegalArgumentException("Content must not be null!");
		}

		this.content.addAll(content);
		this.total = total;
		this.pageable = pageable;
	}

	/**
	 * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being identical
	 * to the entire {@link List}.
	 * 
	 * @param content must not be {@literal null}.
	 */
	public YqPageImpl(List<T> content) {
		this(content, null, null == content ? 0 : content.size());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getNumber()
	 */
	public int getNumber() {
		return pageable == null ? 0 : pageable.getPageNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getSize()
	 */
	public int getSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getTotalPages()
	 */
	public int getTotalPages() {
		return getSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getSize());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getNumberOfElements()
	 */
	public int getNumberOfElements() {
		return content.size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getTotalElements()
	 */
	public long getTotalElements() {
		return total;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#hasPreviousPage()
	 */
	public boolean hasPreviousPage() {
		return getNumber() > 1;
	}

 

 

 

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#iterator()
	 */
	public Iterator<T> iterator() {
		return content.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getContent()
	 */
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#hasContent()
	 */
	public boolean hasContent() {
		return !content.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getSort()
	 */
	public Sort getSort() {
		return pageable == null ? null : pageable.getSort();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String contentType = "UNKNOWN";

		if (content.size() > 0) {
			contentType = content.get(0).getClass().getName();
		}

		return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageImpl<?>)) {
			return false;
		}

		YqPageImpl<?> that = (YqPageImpl<?>) obj;

		boolean totalEqual = this.total == that.total;
		boolean contentEqual = this.content.equals(that.content);
		boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);

		return totalEqual && contentEqual && pageableEqual;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
		result = 31 * result + content.hashCode();

		return result;
	}

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return !hasPreviousPage();
	}

	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return !hasNext();
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		//return (getNumber() + 1) * getSize() < total;
		return (getNumber() ) * getSize() < total;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return this.hasPreviousPage();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Slice#nextPageable()
	 */
	public Pageable nextPageable() {
		return hasNext() ? pageable.next() : Pageable.unpaged();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Slice#previousPageable()
	 */
	public Pageable previousPageable() {
		return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Slice#transform(org.springframework.core.convert.converter.Converter)
	 */
	@Override
	public <U> Page<U> map(Function<? super T, ? extends U> converter) {
		return new PageImpl<>(getConvertedContent(converter), getPageable(), total);
	}
	
	/**
	 * Applies the given {@link Function} to the content of the {@link Chunk}.
	 * 
	 * @param converter must not be {@literal null}.
	 * @return
	 */
	protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

		Assert.notNull(converter, "Function must not be null!");

		return this.stream().map(converter::apply).collect(Collectors.toList());
	}

}
