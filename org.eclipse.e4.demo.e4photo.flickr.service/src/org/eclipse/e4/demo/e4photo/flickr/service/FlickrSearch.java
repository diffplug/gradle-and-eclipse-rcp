/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.e4photo.flickr.service;

/**
 * Represents a flickr search which is used to access search pages
 */
public class FlickrSearch {
	private int pages;
	private int pageSize;
	private int totalItems;
	private String apiKey;

	public FlickrSearch(String apiKey, int pages, int pageSize, int totalItems) {
		this.apiKey = apiKey;
		this.pages = pages;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
	}

	public int getPages() {
		return pages;
	}

	public String getApiKey() {
		return apiKey;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalItems() {
		return totalItems;
	}
}
