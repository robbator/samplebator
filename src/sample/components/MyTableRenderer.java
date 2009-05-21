package sample.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import org.apache.myfaces.shared_impl.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_impl.util.ArrayUtils;
import org.apache.myfaces.shared_impl.util.StringUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MyTableRenderer extends HtmlRenderer {
	/** Header facet name. */
	protected static final String HEADER_FACET_NAME = "header";
	/** Footer facet name. */
	protected static final String FOOTER_FACET_NAME = "footer";
	protected static final String CAPTION_FACET_NAME = "caption";
	/** The logger. */
	private static final Log log = LogFactory.getLog(MyTableRenderer.class);
	/**
	 * @param component
	 *            dataTable
	 * @return number of layout columns
	 */
	protected int getNewspaperColumns(UIComponent component) {
		return 1;
	}
	/**
	 * @param component
	 *            dataTable
	 * @return component to display between layout columns
	 */
	protected UIComponent getNewspaperTableSpacer(UIComponent component) {
		return null;
	}
	/**
	 * @param component
	 *            dataTable
	 * @return whether dataTable has component to display between layout columns
	 */
	protected boolean hasNewspaperTableSpacer(UIComponent component) {
		return false;
	}
	/**
	 * @param component
	 *            dataTable
	 * @return whether dataTable has newspaper columns layed out horizontally
	 */
	protected boolean isNewspaperHorizontalOrientation(UIComponent component) {
		return false;
	}
	/**
	 * @see javax.faces.render.Renderer#getRendersChildren()
	 */
	public boolean getRendersChildren() {
		return true;
	}
	/**
	 * Render the necessary bits that come before any actual <i>rows</i> in the table.
	 * 
	 * @see javax.faces.render.Renderer#encodeBegin(FacesContext, UIComponent)
	 */
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);
		beforeTable(facesContext, (UIData) uiComponent);
		startTable(facesContext, uiComponent);
	}
	/**
	 * actually render the start of the table
	 */
	protected void startTable(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		HtmlRendererUtils.writePrettyLineSeparator(facesContext);
		writer.startElement(HTML.TABLE_ELEM, uiComponent);
		HtmlRendererUtils.writeIdIfNecessary(writer, uiComponent, facesContext);
		HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, org.apache.myfaces.shared_impl.renderkit.html.HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
	}
	/**
	 * Render the TBODY section of the html table. See also method encodeInnerHtml.
	 * 
	 * @see javax.faces.render.Renderer#encodeChildren(FacesContext, UIComponent)
	 */
	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
		RendererUtils.checkParamValidity(facesContext, component, UIData.class);
		ResponseWriter writer = facesContext.getResponseWriter();
		beforeBody(facesContext, (UIData) component);
		HtmlRendererUtils.writePrettyLineSeparator(facesContext);
		renderCaptionFacet(facesContext, writer, component);
		writer.startElement(HTML.TBODY_ELEM, component);
		writer.writeAttribute(HTML.ID_ATTR, component.getClientId(facesContext) + ":tbody_element", null);
		encodeInnerHtml(facesContext, component);
		writer.endElement(HTML.TBODY_ELEM);
		afterBody(facesContext, (UIData) component);
	}
	/**
	 * Renders the caption facet.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the parent <code>UIComponent</code> containing the facets.
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderCaptionFacet(FacesContext facesContext, ResponseWriter writer, UIComponent component) throws IOException {
		UIComponent facet = (UIComponent) component.getFacets().get(CAPTION_FACET_NAME);
		if (facet != null) {
			HtmlRendererUtils.writePrettyLineSeparator(facesContext);
			writer.startElement(HTML.CAPTION_ELEM, component);
			RendererUtils.renderChild(facesContext, facet);
			writer.endElement(HTML.CAPTION_ELEM);
		}
	}
	/**
	 * Gets styles for the specified component.
	 */
	protected static Styles getStyles(UIData uiData) {
		String rowClasses;
		String columnClasses;
		if (uiData instanceof HtmlDataTable) {
			rowClasses = ((HtmlDataTable) uiData).getRowClasses();
			columnClasses = ((HtmlDataTable) uiData).getColumnClasses();
		} else {
			rowClasses = (String) uiData.getAttributes().get(JSFAttr.ROW_CLASSES_ATTR);
			columnClasses = (String) uiData.getAttributes().get(JSFAttr.COLUMN_CLASSES_ATTR);
		}
		return new Styles(rowClasses, columnClasses);
	}
	/**
	 * Class manages the styles from String lists.
	 */
	protected static class Styles {
		private String[] _columnStyle;
		private String[] _rowStyle;
		Styles(String rowStyles, String columnStyles) {
			_rowStyle = (rowStyles == null) ? ArrayUtils.EMPTY_STRING_ARRAY : StringUtils.trim(StringUtils.splitShortString(rowStyles, ','));
			_columnStyle = (columnStyles == null) ? ArrayUtils.EMPTY_STRING_ARRAY : StringUtils.trim(StringUtils.splitShortString(columnStyles, ','));
		}
		public String getRowStyle(int idx) {
			if (!hasRowStyle()) {
				return null;
			}
			return _rowStyle[idx % _rowStyle.length];
		}
		public String getColumnStyle(int idx) {
			if (!hasColumnStyle()) {
				return null;
			}
			return _columnStyle[idx % _columnStyle.length];
		}
		public boolean hasRowStyle() {
			return _rowStyle.length > 0;
		}
		public boolean hasColumnStyle() {
			return _columnStyle.length > 0;
		}
	}
	/**
	 * Renders everything inside the TBODY tag by iterating over the row objects between offsets first and first+rows
	 * and applying the UIColumn components to those objects.
	 * <p>
	 * This method is separated from the encodeChildren so that it can be overridden by subclasses. One class that uses
	 * this functionality is autoUpdateDataTable.
	 */
	public void encodeInnerHtml(FacesContext facesContext, UIComponent component) throws IOException {
		UIData uiData = (UIData) component;
		ResponseWriter writer = facesContext.getResponseWriter();
		int rowCount = uiData.getRowCount();
		if (rowCount == 0) {
			// nothing to render, to get valid xhtml we render an empty dummy row
			writer.startElement(HTML.TR_ELEM, uiData);
			writer.startElement(HTML.TD_ELEM, uiData);
			writer.endElement(HTML.TD_ELEM);
			writer.endElement(HTML.TR_ELEM);
			return;
		}
		// begin the table
		// get the CSS styles
		Styles styles = getStyles(uiData);
		int first = uiData.getFirst();
		int rows = uiData.getRows();
		int last;
		if (rows <= 0) {
			last = rowCount;
		} else {
			last = first + rows;
			if (last > rowCount) {
				last = rowCount;
			}
		}
		int newspaperColumns = getNewspaperColumns(component);
		int newspaperRows;
		if ((last - first) % newspaperColumns == 0)
			newspaperRows = (last - first) / newspaperColumns;
		else
			newspaperRows = ((last - first) / newspaperColumns) + 1;
		boolean newspaperHorizontalOrientation = isNewspaperHorizontalOrientation(component);
		// walk through the newspaper rows
		for (int nr = 0; nr < newspaperRows; nr++) {
			beforeRow(facesContext, uiData); // TODO tutaj pewnie header
			// walk through the newspaper columns
			for (int nc = 0; nc < newspaperColumns; nc++) {
				// the current row in the 'real' table
				int currentRow;
				if (newspaperHorizontalOrientation)
					currentRow = nr * newspaperColumns + nc + first;
				else
					currentRow = nc * newspaperRows + nr + first;
				// if this row is not to be rendered
				if (currentRow >= last)
					continue;
				// bail if any row does not exist
				uiData.setRowIndex(currentRow);
				if (!uiData.isRowAvailable()) {
					log.error("Row is not available. Rowindex = " + currentRow);
					break;
				}
				if (nc == 0) {
					// first column in table, start new row
					beforeRow(facesContext, uiData);
					HtmlRendererUtils.writePrettyLineSeparator(facesContext);
					renderRowStart(facesContext, writer, uiData, styles, nr);
				}
				List children = getChildren(component);
				for (int j = 0; j < newspaperRows; j++) {
					uiData.setRowIndex(j);
					if (nr < children.size()) {
						UIComponent child = (UIComponent) children.get(nr);
						if (child.isRendered()) {
							if (j == 0 && child.getFacet("header") != null) {
								renderRowHeader(writer, child, uiData, child.getFacet("header"));
							}
							boolean columnRendering = child instanceof UIColumn;
							if (columnRendering) {
								beforeColumn(facesContext, uiData, j);
							}
							encodeColumnChild(facesContext, writer, uiData, child, styles, nc * uiData.getChildCount() + j);
							if (columnRendering) {
								afterColumn(facesContext, uiData, j);
							}
						}
					}
				}
				if (hasNewspaperTableSpacer(uiData)) {
					// draw the spacer facet
					if (nc < newspaperColumns - 1)
						renderSpacerCell(facesContext, writer, uiData);
				}
			}
			renderRowEnd(facesContext, writer, uiData);
			afterRow(facesContext, uiData);
		}
	}
	private void renderRowHeader(ResponseWriter writer, UIComponent child, UIData uiData, UIComponent facet) throws IOException {
		writer.startElement(HTML.TH_ELEM, uiData);
		RendererUtils.renderChild(FacesContext.getCurrentInstance(), facet);
		writer.endElement(HTML.TH_ELEM);
	}
	protected void encodeColumnChild(FacesContext facesContext, ResponseWriter writer, UIData uiData, UIComponent component, Styles styles, int columnStyleIndex) throws IOException {
		if (component instanceof UIColumn) {
			renderColumnBody(facesContext, writer, uiData, component, styles, columnStyleIndex);
		}
	}
	/**
	 * Renders the body of a given <code>UIColumn</code> (everything but the header and footer facets). This emits a TD
	 * cell, whose contents are the result of calling encodeBegin, encodeChildren and encodeEnd methods on the component
	 * (or its associated renderer).
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @param component
	 *            the <code>UIComponent</code> to render.
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderColumnBody(FacesContext facesContext, ResponseWriter writer, UIData uiData, UIComponent component, Styles styles, int columnStyleIndex) throws IOException {
		writer.startElement(HTML.TD_ELEM, uiData);
		if (styles.hasColumnStyle()) {
			writer.writeAttribute(HTML.CLASS_ATTR, styles.getColumnStyle(columnStyleIndex), null);
		}
		RendererUtils.renderChild(facesContext, component);
		writer.endElement(HTML.TD_ELEM);
	}
	/**
	 * Renders the start of a new row of body content.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @throws IOException
	 *             if an exceptoin occurs.
	 */
	protected void renderRowStart(FacesContext facesContext, ResponseWriter writer, UIData uiData, Styles styles, int rowStyleIndex) throws IOException {
		writer.startElement(HTML.TR_ELEM, uiData);
		renderRowStyle(facesContext, writer, uiData, styles, rowStyleIndex);
		Object rowId = uiData.getAttributes().get(org.apache.myfaces.shared_impl.renderkit.JSFAttr.ROW_ID);
		if (rowId != null) {
			writer.writeAttribute(HTML.ID_ATTR, rowId.toString(), null);
		}
	}
	protected void renderRowStyle(FacesContext facesContext, ResponseWriter writer, UIData uiData, Styles styles, int rowStyleIndex) throws IOException {
		if (styles.hasRowStyle()) {
			String rowStyle = styles.getRowStyle(rowStyleIndex);
			writer.writeAttribute(HTML.CLASS_ATTR, rowStyle, null);
		}
	}
	/**
	 * Renders the end of a row of body content.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @throws IOException
	 *             if an exceptoin occurs.
	 */
	protected void renderRowEnd(FacesContext facesContext, ResponseWriter writer, UIData uiData) throws IOException {
		writer.endElement(HTML.TR_ELEM);
	}
	/**
	 * Perform any operations necessary immediately before the TABLE start tag is output.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void beforeTable(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * Perform any operations necessary after TABLE start tag is output but before the TBODY start tag.
	 * <p>
	 * This method generates the THEAD/TFOOT sections of a table if there are any header or footer facets defined on the
	 * table or on any child UIColumn component.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void beforeBody(FacesContext facesContext, UIData uiData) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		HtmlRendererUtils.renderTableCaption(facesContext, writer, uiData);
		//renderFacet(facesContext, writer, uiData, true);
		//renderFacet(facesContext, writer, uiData, false);
	}
	/**
	 * Perform any operations necessary immediately before each TR start tag is output.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void beforeRow(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately after each TR end tag is output.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void afterRow(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately before each column child is rendered
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @param columnIndex
	 *            the index of the currenly rendered column
	 */
	protected void beforeColumn(FacesContext facesContext, UIData uiData, int columnIndex) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately after each column child is rendered
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @param columnIndex
	 *            the index of the currenly rendered column
	 */
	protected void afterColumn(FacesContext facesContext, UIData uiData, int columnIndex) throws IOException {
	}
	/**
	 *Perform any operations necessary immediately before each column child's header or footer is rendered
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @param header
	 *            true if the header of the column child is rendered
	 * @param columnIndex
	 *            the index of the currenly rendered column
	 */
	protected void beforeColumnHeaderOrFooter(FacesContext facesContext, UIData uiData, boolean header, int columnIndex) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately after each column child's header of footer is rendered
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 * @param header
	 *            true if the header of the column child is rendered
	 * @param columnIndex
	 *            the index of the currenly rendered column
	 */
	protected void afterColumnHeaderOrFooter(FacesContext facesContext, UIData uiData, boolean header, int columnIndex) throws IOException {
	}
	/**
	 * Perform any operations necessary in the TBODY start tag.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void inBodyStart(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately after the TBODY end tag is output.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void afterBody(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * Perform any operations necessary immediately after the TABLE end tag is output.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param uiData
	 *            the <code>UIData</code> being rendered.
	 */
	protected void afterTable(FacesContext facesContext, UIData uiData) throws IOException {
	}
	/**
	 * @see javax.faces.render.Renderer#encodeEnd(FacesContext, UIComponent)
	 */
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		RendererUtils.checkParamValidity(facesContext, uiComponent, UIData.class);
		endTable(facesContext, uiComponent);
		afterTable(facesContext, (UIData) uiComponent);
	}
	/**
	 * actually render the end of the table
	 */
	protected void endTable(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.endElement(HTML.TABLE_ELEM);
		HtmlRendererUtils.writePrettyLineSeparator(facesContext);
	}
	/**
	 * Renders either the header or the footer facets for the UIData component and all the child UIColumn components, as
	 * a THEAD or TFOOT element containing TR (row) elements.
	 * <p>
	 * If there is a header or footer attached to the UIData then that is rendered as a TR element whose COLSPAN is the
	 * sum of all rendered columns in the table. This allows that header/footer to take up the entire width of the
	 * table.
	 * <p>
	 * If any child column has a header or footer then a TR is rendered with a TH cell for each column child.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the UIData component
	 * @param header
	 *            whether this is the header facet (if not, then the footer facet).
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderFacet(FacesContext facesContext, ResponseWriter writer, UIComponent component, boolean header) throws IOException {
		int colspan = 0;
		boolean hasColumnFacet = false;
		for (Iterator it = getChildren(component).iterator(); it.hasNext();) {
			UIComponent uiComponent = (UIComponent) it.next();
			if (uiComponent.isRendered()) {
				// a UIColumn has a span of 1, anything else has a span of 0
				colspan += determineChildColSpan(uiComponent);
				// hasColumnFacet is true if *any* child column has a facet of
				// the specified type.
				if (!hasColumnFacet) {
					hasColumnFacet = hasFacet(header, uiComponent);
				}
			}
		}
		UIComponent facet = header ? (UIComponent) component.getFacets().get(HEADER_FACET_NAME) : (UIComponent) component.getFacets().get(FOOTER_FACET_NAME);
		if (facet != null || hasColumnFacet) {
			// Header or Footer present on either the UIData or a column, so we
			// definitely need to render the THEAD or TFOOT section.
			String elemName = determineHeaderFooterTag(facesContext, component, header);
			HtmlRendererUtils.writePrettyLineSeparator(facesContext);
			if (elemName != null) {
				writer.startElement(elemName, component);
			}
			if (header) {
				String headerStyleClass = getHeaderClass(component);
				if (facet != null)
					renderTableHeaderRow(facesContext, writer, component, facet, headerStyleClass, colspan);
				if (hasColumnFacet)
					renderColumnHeaderRow(facesContext, writer, component, headerStyleClass);
			} else {
				String footerStyleClass = getFooterClass(component);
				if (hasColumnFacet)
					renderColumnFooterRow(facesContext, writer, component, footerStyleClass);
				if (facet != null)
					renderTableFooterRow(facesContext, writer, component, facet, footerStyleClass, colspan);
			}
			if (elemName != null) {
				writer.endElement(elemName);
			}
		}
	}
	protected String determineHeaderFooterTag(FacesContext facesContext, UIComponent component, boolean header) {
		return header ? HTML.THEAD_ELEM : HTML.TFOOT_ELEM;
	}
	/**
	 * @param header
	 * @param uiComponent
	 * @return boolean
	 */
	protected boolean hasFacet(boolean header, UIComponent uiComponent) {
		if (uiComponent instanceof UIColumn) {
			UIColumn uiColumn = (UIColumn) uiComponent;
			return header ? uiColumn.getHeader() != null : uiColumn.getFooter() != null;
		}
		return false;
	}
	/**
	 * Calculate the number of columns the specified child component will span when rendered.
	 * <p>
	 * Normally, this is a fairly simple calculation: a UIColumn component is rendered as one column, every other child
	 * type is not rendered (ie spans zero columns). However custom subclasses of this renderer may override this method
	 * to handle cases where a single component renders as multiple columns.
	 */
	protected int determineChildColSpan(UIComponent uiComponent) {
		if (uiComponent instanceof UIColumn) {
			return 1;
		}
		return 0;
	}
	/**
	 * Renders the header row of the table being rendered.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the <code>UIComponent</code> for whom a table is being rendered.
	 * @param headerFacet
	 *            the facet for the header.
	 * @param headerStyleClass
	 *            the styleClass of the header.
	 * @param colspan
	 *            the number of columns the header should span. Typically, this is the number of columns in the table.
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderTableHeaderRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, UIComponent headerFacet, String headerStyleClass, int colspan) throws IOException {
		renderTableHeaderOrFooterRow(facesContext, writer, component, headerFacet, headerStyleClass, determineHeaderCellTag(facesContext, component), colspan, true);
	}
	/**
	 * Renders the footer row of the table being rendered.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the <code>UIComponent</code> for whom a table is being rendered.
	 * @param footerFacet
	 *            the facet for the footer.
	 * @param footerStyleClass
	 *            the styleClass of the footer.
	 * @param colspan
	 *            the number of columns the header should span. Typically, this is the number of columns in the table.
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderTableFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, UIComponent footerFacet, String footerStyleClass, int colspan) throws IOException {
		renderTableHeaderOrFooterRow(facesContext, writer, component, footerFacet, footerStyleClass, HTML.TD_ELEM, colspan, false);
	}
	/**
	 * Renders the header row for the columns, which is a separate row from the header row for the <code>UIData</code>
	 * header facet.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the UIData component for whom a table is being rendered.
	 * @param headerStyleClass
	 *            the styleClass of the header
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderColumnHeaderRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, String headerStyleClass) throws IOException {
		renderColumnHeaderOrFooterRow(facesContext, writer, component, headerStyleClass, true);
	}
	/**
	 * Renders the footer row for the columns, which is a separate row from the footer row for the <code>UIData</code>
	 * footer facet.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param component
	 *            the <code>UIComponent</code> for whom a table is being rendered.
	 * @param footerStyleClass
	 *            the styleClass of the footerStyleClass
	 * @throws IOException
	 *             if an exception occurs.
	 */
	protected void renderColumnFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, String footerStyleClass) throws IOException {
		renderColumnHeaderOrFooterRow(facesContext, writer, component, footerStyleClass, false);
	}
	protected void renderTableHeaderOrFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, UIComponent facet, String styleClass, String colElementName, int colspan, boolean isHeader) throws IOException {
		HtmlRendererUtils.writePrettyLineSeparator(facesContext);
		writer.startElement(HTML.TR_ELEM, component);
		writer.startElement(colElementName, component);
		if (colElementName.equals(determineHeaderCellTag(facesContext, component)) && isHeader) {
			writer.writeAttribute(HTML.SCOPE_ATTR, HTML.SCOPE_COLGROUP_VALUE, null);
		}
		// span all the table's columns
		int newsPaperColumns = getNewspaperColumns(component);
		int totalColumns = colspan * newsPaperColumns;
		if (hasNewspaperTableSpacer(component)) {
			totalColumns = totalColumns + newsPaperColumns - 1;
		}
		writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(totalColumns), null);
		if (styleClass != null) {
			writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
		}
		if (facet != null) {
			RendererUtils.renderChild(facesContext, facet);
		}
		writer.endElement(colElementName);
		writer.endElement(HTML.TR_ELEM);
	}
	/**
	 * @param component
	 *            the UIData component for whom a table is being rendered.
	 */
	private void renderColumnHeaderOrFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, String styleClass, boolean header) throws IOException {
		HtmlRendererUtils.writePrettyLineSeparator(facesContext);
		writer.startElement(HTML.TR_ELEM, component);
		int columnIndex = 0;
		int newspaperColumns = getNewspaperColumns(component);
		for (int nc = 0; nc < newspaperColumns; nc++) {
			for (Iterator it = getChildren(component).iterator(); it.hasNext();) {
				UIComponent uiComponent = (UIComponent) it.next();
				if (uiComponent.isRendered()) {
					if (component instanceof UIData && uiComponent instanceof UIColumn)
						beforeColumnHeaderOrFooter(facesContext, (UIData) component, header, columnIndex);
					renderColumnChildHeaderOrFooterRow(facesContext, writer, uiComponent, styleClass, header);
					if (component instanceof UIData && uiComponent instanceof UIColumn)
						afterColumnHeaderOrFooter(facesContext, (UIData) component, header, columnIndex);
				}
				columnIndex += 1;
			}
			if (hasNewspaperTableSpacer(component)) {
				// draw the spacer facet
				if (nc < newspaperColumns - 1)
					renderSpacerCell(facesContext, writer, component);
			}
		}
		writer.endElement(HTML.TR_ELEM);
	}
	/**
	 * Renders a spacer between adjacent newspaper columns.
	 */
	protected void renderSpacerCell(FacesContext facesContext, ResponseWriter writer, UIComponent component) throws IOException {
		UIComponent spacer = getNewspaperTableSpacer(component);
		if (spacer == null)
			return;
		writer.startElement(HTML.TD_ELEM, component);
		RendererUtils.renderChild(facesContext, spacer);
		writer.endElement(HTML.TD_ELEM);
	}
	protected void renderColumnChildHeaderOrFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent, String styleClass, boolean isHeader) throws IOException {
		if (uiComponent instanceof UIColumn) {
			// allow column to override style class, new in JSF 1.2
			if (uiComponent instanceof HtmlColumn) {
				HtmlColumn column = (HtmlColumn) uiComponent;
				if (isHeader && column.getHeaderClass() != null)
					styleClass = column.getHeaderClass();
				else if (!isHeader && column.getFooterClass() != null)
					styleClass = column.getFooterClass();
			} else {
				// This code corrects MYFACES-1790, because HtmlColumnTag
				// has as component type javax.faces.Column, so as side
				// effect it not create HtmlColumn, it create UIColumn
				// classes.
				UIColumn column = (UIColumn) uiComponent;
				if (isHeader) {
					String headerClass = (String) column.getAttributes().get("headerClass");
					if (headerClass != null) {
						styleClass = (String) headerClass;
					}
				} else {
					String footerClass = (String) column.getAttributes().get("footerClass");
					if (footerClass != null) {
						styleClass = (String) footerClass;
					}
				}
			}
			if (isHeader) {
				renderColumnHeaderCell(facesContext, writer, uiComponent, ((UIColumn) uiComponent).getHeader(), styleClass, 0);
			} else {
				renderColumnFooterCell(facesContext, writer, uiComponent, ((UIColumn) uiComponent).getFooter(), styleClass, 0);
			}
		}
	}
	/**
	 * Renders the header facet for the given <code>UIColumn</code>.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiColumn
	 *            the <code>UIColumn</code>.
	 * @param headerStyleClass
	 *            the styleClass of the header facet.
	 * @param colspan
	 *            the colspan for the tableData element in which the header facet will be wrapped.
	 * @throws IOException
	 */
	protected void renderColumnHeaderCell(FacesContext facesContext, ResponseWriter writer, UIColumn uiColumn, String headerStyleClass, int colspan) throws IOException {
		renderColumnHeaderCell(facesContext, writer, uiColumn, uiColumn.getHeader(), headerStyleClass, colspan);
	}
	/**
	 * Renders a TH cell within a TR within a THEAD section. If the specified UIColumn object does have a header facet,
	 * then that facet is rendered within the cell, otherwise the cell is left blank (though any specified style class
	 * is still applied to empty cells).
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiComponent
	 *            the <code>UIComponent</code> to render the facet for.
	 * @param facet
	 *            the <code>UIComponent</code> to render as facet.
	 * @param headerStyleClass
	 *            the styleClass of the header facet.
	 * @param colspan
	 *            the colspan for the tableData element in which the header facet will be wrapped.
	 * @throws IOException
	 */
	protected void renderColumnHeaderCell(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent, UIComponent facet, String headerStyleClass, int colspan) throws IOException {
		writer.startElement(determineHeaderCellTag(facesContext, uiComponent.getParent()), uiComponent);
		if (colspan > 1) {
			writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
		}
		if (headerStyleClass != null) {
			writer.writeAttribute(HTML.CLASS_ATTR, headerStyleClass, null);
		}
		writer.writeAttribute(HTML.SCOPE_ATTR, "col", null);
		if (facet != null) {
			RendererUtils.renderChild(facesContext, facet);
		}
		writer.endElement(determineHeaderCellTag(facesContext, uiComponent.getParent()));
	}
	protected String determineHeaderCellTag(FacesContext facesContext, UIComponent uiComponent) {
		return HTML.TH_ELEM;
	}
	/**
	 * Renders the footer facet for the given <code>UIColumn</code>.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiColumn
	 *            the <code>UIComponent</code>.
	 * @param footerStyleClass
	 *            the styleClass of the footer facet.
	 * @param colspan
	 *            the colspan for the tableData element in which the footer facet will be wrapped.
	 * @throws IOException
	 */
	protected void renderColumnFooterCell(FacesContext facesContext, ResponseWriter writer, UIColumn uiColumn, String footerStyleClass, int colspan) throws IOException {
		renderColumnFooterCell(facesContext, writer, uiColumn, uiColumn.getFooter(), footerStyleClass, colspan);
	}
	/**
	 * Renders the footer facet for the given <code>UIColumn</code>.
	 * 
	 * @param facesContext
	 *            the <code>FacesContext</code>.
	 * @param writer
	 *            the <code>ResponseWriter</code>.
	 * @param uiComponent
	 *            the <code>UIComponent</code> to render the facet for.
	 * @param facet
	 *            the <code>UIComponent</code> to render as facet.
	 * @param footerStyleClass
	 *            the styleClass of the footer facet.
	 * @param colspan
	 *            the colspan for the tableData element in which the footer facet will be wrapped.
	 * @throws IOException
	 */
	protected void renderColumnFooterCell(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent, UIComponent facet, String footerStyleClass, int colspan) throws IOException {
		writer.startElement(HTML.TD_ELEM, uiComponent);
		if (colspan > 1) {
			writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan), null);
		}
		if (footerStyleClass != null) {
			writer.writeAttribute(HTML.CLASS_ATTR, footerStyleClass, null);
		}
		if (facet != null) {
			RendererUtils.renderChild(facesContext, facet);
		}
		writer.endElement(HTML.TD_ELEM);
	}
	/**
	 * Gets the headerClass attribute of the given <code>UIComponent</code>.
	 * 
	 * @param component
	 *            the <code>UIComponent</code>.
	 * @return the headerClass attribute of the given <code>UIComponent</code>.
	 */
	protected static String getHeaderClass(UIComponent component) {
		if (component instanceof HtmlDataTable) {
			return ((HtmlDataTable) component).getHeaderClass();
		} else {
			return (String) component.getAttributes().get(org.apache.myfaces.shared_impl.renderkit.JSFAttr.HEADER_CLASS_ATTR);
		}
	}
	/**
	 * Gets the footerClass attribute of the given <code>UIComponent</code>.
	 * 
	 * @param component
	 *            the <code>UIComponent</code>.
	 * @return the footerClass attribute of the given <code>UIComponent</code>.
	 */
	protected static String getFooterClass(UIComponent component) {
		if (component instanceof HtmlDataTable) {
			return ((HtmlDataTable) component).getFooterClass();
		} else {
			return (String) component.getAttributes().get(org.apache.myfaces.shared_impl.renderkit.JSFAttr.FOOTER_CLASS_ATTR);
		}
	}
	public void decode(FacesContext context, UIComponent component) {
		super.decode(context, component);
	}
}
