package com.yq.xcode.common.audit;

import java.lang.reflect.Method;

public class AuditablePropertyMetaData<T> {
	
	private String name;
	
	private String columnName;
	
	private Class<T> type;
	
	private Method writeMethod;
	
	private Method readMethod;
	
	private boolean hidden = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public Method getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}
	
	
	public Object read(Object instance)  {
		try{
			Object value = readMethod.invoke(instance, null);
			return value;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AuditablePropertyMetaData[");
		buffer.append("name=").append(name)
		.append(", columnName=").append(columnName)
		.append(", type=").append(type.getName())
		.append("]");
		return buffer.toString();
	}
	
	
}
