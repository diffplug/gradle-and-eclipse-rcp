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
package org.eclipse.e4.demo.e4photo.flickr.service.rest;

import org.eclipse.e4.demo.e4photo.flickr.service.FlickrSearch;

public class RestFlickrTagSearch extends FlickrSearch {
	private String tags;
	
	public RestFlickrTagSearch(String apiKey, int pages, int pageSize, int totalItems, String tags) {
		super(apiKey, pages, pageSize, totalItems);
		this.tags = tags;
	}
	
	public String getTags() {
		return tags;
	}
}