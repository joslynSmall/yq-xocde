package com.yq.xcode.common.bean;

public class HColumn {
	
	public enum Format {
		amount,
		yorn,
		html
	}
	
	private String field;
	private String title;
	private String format;
	private String width;
	private String template;
	private String type;
	private boolean isCommand = false;
	private String click;
	private String sortable;
	private String editable;
	private String validation;
	private String encoded;
	private String editor;
	private String headerTemplate;
	private String defaultValue;
	private String attributes;
	private String aggregate;
	private String footerTemplate;
	private String hidden;
	private String sortField;
	
	/**
	 * 多列显示
	 */
	private FieldTitle[] expands;
	
	public HColumn(){
		
	}
	
	public HColumn(String field,String title){
		this.title = title;
		this.field = field;
	}
	
	public HColumn(String field,String title, String width){
		this.title = title;
		this.width = width;
		this.field = field;
	}
	
	public HColumn(String field,String title, String width, String format){
		this.title = title;
		this.width = width;
		this.field = field;
		this.format = format;
	}
	
	public HColumn(String field,String title, String width, String format, String template){
		this.title = title;
		this.width = width;
		this.field = field;
		this.format = format;
		this.template = template;
	}
	
	public HColumn(String field,String title, String width,FieldTitle[] expands){
		this.title = title;
		this.width = width;
		this.field = field;
		this.expands = expands;
	}
	
	public HColumn(String field){
		this.field = field;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}

	public boolean isCommand() {
		return isCommand;
	}

	public void setCommand(boolean isCommand) {
		this.isCommand = isCommand;
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getHeaderTemplate() {
		return headerTemplate;
	}

	public void setHeaderTemplate(String headerTemplate) {
		this.headerTemplate = headerTemplate;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getAggregate() {
		return aggregate;
	}

	public void setAggregate(String aggregate) {
		this.aggregate = aggregate;
	}

	public String getFooterTemplate() {
		return footerTemplate;
	}

	public void setFooterTemplate(String footerTemplate) {
		this.footerTemplate = footerTemplate;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public FieldTitle[] getExpands() {
		return expands;
	}

	public void setExpands(FieldTitle[] expands) {
		this.expands = expands;
	}
	
	
}