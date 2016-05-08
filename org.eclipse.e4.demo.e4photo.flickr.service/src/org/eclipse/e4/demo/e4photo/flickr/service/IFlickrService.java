/*******************************************************************************
 * Copyright (c) 2008, 2009 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.e4photo.flickr.service;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Service interface to access flickr
 */
public interface IFlickrService {
	public FlickrSearch createTagSearch(String apiKey, String tags) throws RemoteException;
	
	public List<FlickrPhoto> getPhotos(FlickrSearch search, int page) throws RemoteException;
	
	public InputStream getPhoto(FlickrPhoto photo) throws RemoteException;
}
