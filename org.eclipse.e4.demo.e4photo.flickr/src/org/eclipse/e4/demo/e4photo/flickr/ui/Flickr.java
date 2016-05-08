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

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.demo.e4photo.flickr.service.FlickrPhoto;
import org.eclipse.e4.demo.e4photo.flickr.service.FlickrSearch;
import org.eclipse.e4.demo.e4photo.flickr.service.IFlickrService;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Search for images on flickr.com
 */
public class Flickr {
	@Inject
	private IFlickrService flickrService;
	
	private Composite comp;
	
	private PagedTable<FlickrPhoto> table;
	
	@Inject
	public Flickr(Composite comp) {
		this.comp = comp;
		comp.setLayout(new GridLayout(3, false));
		
		final Image OK_IMG = new Image(comp.getDisplay(), getClass().getClassLoader().getResourceAsStream("icons/accept.png"));
		final Image NOK_IMG = new Image(comp.getDisplay(), getClass().getClassLoader().getResourceAsStream("icons/exclamation.png"));
		
		comp.addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				OK_IMG.dispose();
				NOK_IMG.dispose();
			}
		});
		
		comp.setBackgroundMode(SWT.INHERIT_DEFAULT);
		
		Label l = new Label(comp, SWT.NONE);
		l.setText("Flickr API-Key");
		l.setLayoutData(new GridData(SWT.DEFAULT,GridData.CENTER,false,false));
		
		final Text apiKey = new Text(comp, SWT.BORDER);
		apiKey.setLayoutData(new GridData(GridData.FILL,GridData.CENTER,true,false,2,1));
		apiKey.setText("46d3d5269fe6513602b3f0f06d9e2b2e");
		
		l = new Label(comp, SWT.NONE);
		l.setText("Search Tags");
		l.setLayoutData(new GridData(SWT.DEFAULT,GridData.CENTER,false,false));
		
		final Text tags = new Text(comp, SWT.BORDER);
		tags.setLayoutData(new GridData(GridData.FILL,GridData.CENTER,true,false));
		tags.setText("eclipsecon");
		
		Button button = new Button(comp, SWT.PUSH);
		button.setText("Search");
		
		table = new PagedTable<FlickrPhoto>(comp, SWT.NONE);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSearch(apiKey.getText(), tags.getText());
			}
		});
		
		table.addColumn("Id", new PagedTable.PageColumnLabelProvider<FlickrPhoto>() {
			@Override
			protected String doGetText(FlickrPhoto element) {
				return element.getId();
			}
		},new ColumnPixelData(120));
		table.addColumn("Title", new PagedTable.PageColumnLabelProvider<FlickrPhoto>() {
			@Override
			protected String doGetText(FlickrPhoto element) {
				return element.getTitle();
			}
			
			@Override
			protected String doGetToolTipText(FlickrPhoto element) {
				return element.getId() +  " - " + element.getTitle();
			}
		},new ColumnWeightData(1));
		table.addColumn("Public", new PagedTable.PageColumnLabelProvider<FlickrPhoto>() {
			@Override
			protected Image doGetImage(FlickrPhoto element) {
				if( element.isPublic() ) {
					return OK_IMG;
				} else {
					return NOK_IMG;
				}
			}
			
			@Override
			protected String doGetText(FlickrPhoto element) {
				return "";
			}
		},new ColumnPixelData(50));
		
		table.addColumn("Friend", new PagedTable.PageColumnLabelProvider<FlickrPhoto>() {
			@Override
			protected Image doGetImage(FlickrPhoto element) {
				if( element.isFriend() ) {
					return OK_IMG;
				} else {
					return NOK_IMG;
				}
			}
			@Override
			protected String doGetText(FlickrPhoto element) {
				return "";
			}

		},new ColumnPixelData(50));
		
		table.addColumn("Family", new PagedTable.PageColumnLabelProvider<FlickrPhoto>() {
			@Override
			protected Image doGetImage(FlickrPhoto element) {
				if( element.isFamily() ) {
					return OK_IMG;
				} else {
					return NOK_IMG;
				}
			}
			@Override
			protected String doGetText(FlickrPhoto element) {
				return "";
			}

		},new ColumnPixelData(50));
		
		table.setLayoutData(new GridData(GridData.FILL,GridData.FILL,true,true,3,1));
		
		new ToolTipSupport(table.getViewer(), ToolTip.NO_RECREATE, false);
	}
	
	private void handleSearch(String apiKey, String tags) {
		try {
			FlickrSearch search = flickrService.createTagSearch(apiKey, tags);
			table.setInput(new SearchInput(search));
		} catch (RemoteException e1) {
			Status s = new Status(IStatus.ERROR, "org.eclipse.e4.demo.e4photo.flickr", e1.getMessage(), e1);
			ErrorDialog.openError(table.getShell(), "Searchfailure", "Failure while executing search", s);
		}
	}
	
	private class ToolTipSupport extends ColumnViewerToolTipSupport {

		protected ToolTipSupport(ColumnViewer viewer, int style,
				boolean manualActivation) {
			super(viewer, style, manualActivation);
		}
		
		@Override
		protected Composite createViewerToolTipContentArea(Event event,
				ViewerCell cell, final Composite parent) {
			Composite comp = new Composite(parent, SWT.NONE);
			
			parent.setBackgroundMode(SWT.INHERIT_DEFAULT);
			if( cell == null || cell.getElement() == null ) {
				return comp;
			}
			final FlickrPhoto photo = (FlickrPhoto) cell.getElement();
			
			comp.setLayout(new GridLayout());
			Label l = new Label(comp, SWT.NONE);
			l.setText(getText(event));
			l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			l = new Label(comp, SWT.SEPARATOR | SWT.HORIZONTAL);
			l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			final Label img = new Label(comp, SWT.NONE);
			
			Thread t = new Thread() {
				public void run() {
					if( parent.isDisposed() ) {
						return;
					}
					
					parent.getDisplay().syncExec(new Runnable() {
						
						public void run() {
							if( img.isDisposed() ) {
								return;
							}
							
							try {
								img.setImage(new Image(parent.getDisplay(), flickrService.getPhoto(photo)));
							} catch (RemoteException e) {
								img.setText("Failed to fetch image from remote server.");
							}
							parent.getShell().pack(true);
						}
					});
				}
			};
			
			t.start();
			
			img.addDisposeListener(new DisposeListener() {
				
				public void widgetDisposed(DisposeEvent e) {
					if( img.getImage() != null ) {
						img.getImage().dispose();
					}
				}
			});
			
			return comp;
		}
		
	}
	
	private class SearchInput implements IPagedInput<FlickrPhoto> {
		private FlickrSearch search;
		
		public SearchInput(FlickrSearch search) {
			this.search = search;
		}
		
		public int getPages() {
			return search.getPages();
		}

		public int getPageSize() {
			return search.getPageSize();
		}

		public int getTotalItems() {
			return search.getTotalItems();
		}

		public List<FlickrPhoto> getItems(int page) {
			try {
				return flickrService.getPhotos(search, page);
			} catch (RemoteException e) {
				Status s = new Status(IStatus.ERROR, "org.eclipse.e4.demo.e4photo.flickr", e.getMessage(), e);
				ErrorDialog.openError(comp.getShell(), "Searchfailure", "Failure while fetching photo page", s);
			}
			
			return Collections.emptyList();
		}
		
	}
}
