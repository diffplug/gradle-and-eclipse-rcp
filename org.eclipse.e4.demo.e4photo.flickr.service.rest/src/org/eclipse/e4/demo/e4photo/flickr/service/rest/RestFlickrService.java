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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.demo.e4photo.flickr.service.FlickrPhoto;
import org.eclipse.e4.demo.e4photo.flickr.service.FlickrSearch;
import org.eclipse.e4.demo.e4photo.flickr.service.IFlickrService;

/**
 * Custom implementation to access flickr through its rest API
 */
public class RestFlickrService implements IFlickrService {
	
	public RestFlickrService() {
		System.err.println("Service created");
	}
	
	public InputStream getPhoto(FlickrPhoto photo) throws RemoteException {
		try {
			String path = "http://farm" + photo.getFarm() + ".static.flickr.com/" + photo.getServer() + "/"+ photo.getId() + "_" + photo.getSecret() + ".jpg";
			URL url = new URL(path);
			return url.openStream();
		} catch (MalformedURLException e) {
			throw new RemoteException("Failed to fetch Flickr-Image.", e);
		} catch (IOException e) {
			throw new RemoteException("Failed to fetch Flickr-Image.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public FlickrSearch createTagSearch(String apiKey, String tags) throws RemoteException {
		Map<String,Object> obj = searchByTagsRequest("flickr.photos.search", apiKey, tags, 1);
		if( obj != null ) {
			try {
				Map<String,Object> o = (Map<String, Object>) obj.get("photos");
				int pages = ((Number) o.get("pages")).intValue();
				int pageSize = ((Number) o.get("perpage")).intValue();
				int total = Integer.parseInt((String) o.get("total"));
				return new RestFlickrTagSearch( apiKey, pages, pageSize, total, tags);
			} catch (Exception e) {
				throw new RemoteException("Failure while parsing response", e);
			}	
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<FlickrPhoto> getPhotos(FlickrSearch search, int page) throws RemoteException {
		if( search instanceof RestFlickrTagSearch ) {
			RestFlickrTagSearch tmp = (RestFlickrTagSearch) search;
			Map<String,Object> root = searchByTagsRequest("flickr.photos.search", tmp.getApiKey(), tmp.getTags(), page);
			if( root != null ) {
				try {
					Collection<Object> list = (Collection<Object>) ((Map<String,Object>)root.get("photos")).get("photo");
					ArrayList<FlickrPhoto> rv = new ArrayList<FlickrPhoto>();
					Iterator<Object> it = list.iterator();
					
					while( it.hasNext() ) {
						Map<String,Object> o = (Map<String, Object>) it.next();
						FlickrPhoto photo = new FlickrPhoto();
						photo.setFamily( ((Number)o.get("isfamily")).intValue() != 0);
						photo.setFarm( ((Number)o.get("farm")).intValue() );
						photo.setFriend(((Number)o.get("isfriend")).intValue() != 0);
						photo.setId((String) o.get("id"));
						photo.setOwner((String) o.get("owner"));
						photo.setPublic(((Number)o.get("ispublic")).intValue() != 0);
						photo.setSecret((String) o.get("secret"));
						photo.setServer((String) o.get("server"));
						photo.setTitle((String) o.get("title"));
						rv.add(photo);
					}
					return rv;
				} catch (Exception e) {
					throw new RemoteException("Failure while parsing response", e);
				}	
			}
		}
		
		throw new IllegalArgumentException("The search type '"+search.getClass().getName()+"' is not supported.");
	}

	@SuppressWarnings("unchecked")
	private Map<String,Object> searchByTagsRequest(String method, String apiKey, String tags, int page) throws RemoteException {
		String request = "http://api.flickr.com/services/rest/";
		request += "?tags=" + tags;
		request += "&method=flickr.photos.search";
		request += "&api_key="+apiKey;
		request += "&format=json";
		request += "&page=" + page;
		
		try {
			URL url = new URL(request);
			InputStream in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			StringBuilder b = new StringBuilder();
			while( (line = reader.readLine()) != null ) {
				b.append(line);
			}
			
			String result = b.toString().substring("jsonFlickrApi(".length(), b.toString().length() - 1);
			System.err.println("RESULT: " + b);
			Map<String,Object> o = (Map<String, Object>) JSONUtil.read(result);
			
			if( ! "ok".equals(o.get("stat")) ) {
				throw new RemoteException((String)o.get("message"));
			}
			return o;
		} catch (RemoteException e) {
			throw e;
		} catch (MalformedURLException e) {
			throw new RemoteException("Failure fetching page '"+page+"'",e); 
		} catch (IOException e) {
			throw new RemoteException("Failure fetching page '"+page+"'",e);
		}
	}
	
	public static void main(String[] args) {
		try {
			RestFlickrService s = new RestFlickrService();
			FlickrSearch search = s.createTagSearch("46d3d5269fe6513602b3f0f06d9e2b2e", "eclipsecon");
			
			for( int page = 1; page <= search.getPages(); page++ ) {
				System.err.println("--------------------------------");
				System.err.println("Page " + page);
				System.err.println("--------------------------------");
				List<FlickrPhoto> photos = s.getPhotos(search, page);
				for( FlickrPhoto p : photos ) {
					System.err.println("	* " + p);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
