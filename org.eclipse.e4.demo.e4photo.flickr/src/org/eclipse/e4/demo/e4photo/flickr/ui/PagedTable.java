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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

/**
 * A table which shows it's items in a paged list
 * 
 * @param <M> the object type
 */
public class PagedTable<M> extends Composite {
	private TableViewer viewer;
	private IPagedInput<M> input;
	private Label pageLabel;
	private TableColumnLayout tblLayout;
	private Composite layoutFooter;
	private Composite progressComp;
	private Text pageField;
	private int currentPage;

	public PagedTable(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());

		Composite tabelContainer = new Composite(this, SWT.NONE);
		tabelContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		tblLayout = new TableColumnLayout();
		tabelContainer.setLayout(tblLayout);

		viewer = new TableViewer(tabelContainer);
		viewer.getTable().setHeaderVisible(true);

		layoutFooter = new Composite(this, SWT.NONE);
		layoutFooter.setLayout(new GridLayout(2, false));
		layoutFooter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite navigation = new Composite(layoutFooter, SWT.NONE);
		navigation.setLayout(new GridLayout(8, false));

		Image imgFirst = new Image(navigation.getDisplay(), getClass()
				.getClassLoader().getResourceAsStream(
						"icons/control_start_blue.png"));
		Image imgPrev = new Image(navigation.getDisplay(), getClass()
				.getClassLoader().getResourceAsStream(
						"icons/control_rewind_blue.png"));
		Image imgNext = new Image(navigation.getDisplay(), getClass()
				.getClassLoader().getResourceAsStream(
						"icons/control_fastforward_blue.png"));
		Image imgLast = new Image(navigation.getDisplay(), getClass()
				.getClassLoader().getResourceAsStream(
						"icons/control_end_blue.png"));

		Button b = new Button(navigation, SWT.PUSH | SWT.FLAT);
		b.setImage(imgFirst);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (input != null) {
					showPage(1);
				}
			}
		});

		b = new Button(navigation, SWT.PUSH | SWT.FLAT);
		b.setImage(imgPrev);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (currentPage > 1) {
					if (input != null) {
						showPage(--currentPage);
					}
				}

			}
		});

		Label l = new Label(navigation, SWT.NONE);
		l.setText("Page");

		pageField = new Text(navigation, SWT.BORDER);
		GridData gd = new GridData(50, SWT.DEFAULT);
		gd.verticalAlignment = SWT.CENTER;
		pageField.setLayoutData(gd);
		pageField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					if (input != null) {
						int i = Integer.parseInt(pageField.getText());
						if (i >= 1 && i <= input.getPages()) {
							showPage(i);
						} else {
							pageField.setText(currentPage+"");
						}
					}
				} catch (Exception ex) {
					Status s = new Status(IStatus.ERROR, "org.eclipse.e4.demo.e4photo.flickr", ex.getMessage(), ex);
					ErrorDialog.openError(pageField.getShell(), "Invalid number", "The entere value is not a valid page number", s);
					pageField.setText(currentPage+"");
				}

			}
		});

		pageLabel = new Label(navigation, SWT.NONE);
		pageLabel.setText("of ");

		b = new Button(navigation, SWT.PUSH | SWT.FLAT);
		b.setImage(imgNext);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (input != null) {
					if (currentPage < input.getPages()) {
						showPage(++currentPage);
					}
				}
			}
		});

		b = new Button(navigation, SWT.PUSH | SWT.FLAT);
		b.setImage(imgLast);
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (input != null) {
					showPage(input.getPages());
				}
			}
		});

		progressComp = new Composite(this, SWT.NONE);
		progressComp.setLayout(new GridLayout(2, false));
		progressComp.setVisible(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.exclude = true;
		progressComp.setLayoutData(gd);

		l = new Label(progressComp, SWT.NONE);
		l.setText("Loading");

		ProgressBar progress = new ProgressBar(progressComp, SWT.INDETERMINATE);
		progress.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		viewer.setContentProvider(new ArrayContentProvider());
	}

	public ColumnViewer getViewer() {
		return viewer;
	}

	public void addColumn(String label, PageColumnLabelProvider<M> labelProvider,
			ColumnLayoutData layoutData) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(label);
		column.setLabelProvider(labelProvider);
		tblLayout.setColumnData(column.getColumn(), layoutData);
	}

	public void setInput(IPagedInput<M> input) {
		this.input = input;
		pageLabel.setText("of " + input.getPages());
		showPage(1);
	}

	public void showPage(final int page) {
		((GridData) layoutFooter.getLayoutData()).exclude = true;
		((GridData) progressComp.getLayoutData()).exclude = false;
		layoutFooter.setVisible(false);
		progressComp.setVisible(true);
		layout(true, true);
		Thread t = new Thread() {
			@Override
			public void run() {
				final List<M> list = input.getItems(page);
				getDisplay().syncExec(new Runnable() {

					public void run() {
						currentPage = page;
						((GridData) layoutFooter.getLayoutData()).exclude = false;
						((GridData) progressComp.getLayoutData()).exclude = true;
						layoutFooter.setVisible(true);
						progressComp.setVisible(false);
						layout(true, true);
						viewer.setInput(list);
						pageField.setText(page + "");
					}
				});
			}
		};
		t.start();
	}
	
	public static class PageColumnLabelProvider<M> extends ColumnLabelProvider {
		@SuppressWarnings("unchecked")
		@Override
		public final String getText(Object element) {
			return doGetText((M) element);
		}
		
		protected String doGetText(M element) {
			return super.getText(element);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public final Image getImage(Object element) {
			return doGetImage((M) element);
		}
		
		protected Image doGetImage(M element) {
			return super.getImage(element);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public String getToolTipText(Object element) {
			return doGetToolTipText((M) element);
		}
		
		protected String doGetToolTipText(M element) {
			return super.getToolTipText(element);
		}
	}
	
}
