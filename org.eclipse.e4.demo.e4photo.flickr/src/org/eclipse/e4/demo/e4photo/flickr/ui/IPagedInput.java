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
package org.eclipse.e4.demo.e4photo.flickr.ui;

import java.util.List;

/**
 * Input for the paged table
 * 
 * @param <M>
 *            the paged table
 */
public interface IPagedInput<M> {
	/**
	 * @return the number of pages
	 */
	public int getPages();

	/**
	 * @return the number of elements per page
	 */
	public int getPageSize();

	/**
	 * @return the number of total items per page
	 */
	public int getTotalItems();

	/**
	 * Get the items of the page
	 * 
	 * @param page
	 *            the page
	 * @return the items
	 */
	public List<M> getItems(int page);
}
