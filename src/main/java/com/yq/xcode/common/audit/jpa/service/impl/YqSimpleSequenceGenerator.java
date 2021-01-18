package com.yq.xcode.common.audit.jpa.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yq.xcode.common.audit.jpa.service.YqSequenceGenerator; 
 
public class YqSimpleSequenceGenerator  implements YqSequenceGenerator,InitializingBean{
	@Qualifier("dataSource")
	@Autowired
	private DataSource dataSource;
	
	private String tableName = "ID_GENERATOR";
	
	private String idColumnName = "ID_KEY";
	
	private String valueColumnName = "ID_VALUE";
	
	private String sqlInsert = "";
	
	private String sqlUpdate = "";
	
	private int allowInterval = 10;
	
	private int initAllowInterval = 10;
	
	private String sqlGet = "";
	
	private boolean useForUpdate = true;
	
	private boolean allowPreallocate = true;
	
	private JdbcTemplate jdbcTemplate;
	
	private ConcurrentMap<String, PreallocatedSequence> preallocatedSequences = new ConcurrentHashMap<String, PreallocatedSequence>();
	
	@Override
	public void afterPropertiesSet() throws Exception{
//		System.out.println("ISOLATION LEVEL "+ dataSource.getConnection().getTransactionIsolation());
		if(dataSource == null && jdbcTemplate == null) {
			throw new java.lang.IllegalStateException("Neither datasource nor jdbcTemplate is set in SimpleSequenceGenerator");
		}
		sqlGet = 
			new StringBuffer("select ")
			.append(valueColumnName)
			.append(" from ")
			.append(tableName)
			.append(" where ")
			.append(idColumnName)
			.append("=?")
			.append(useForUpdate?" for update":"").toString();
		sqlUpdate = 
			new StringBuffer("update ")
			.append(tableName)
			.append(" set ")
			.append(valueColumnName)
			.append("=? where ")
			.append(idColumnName)
			.append("=? and ")
			.append(valueColumnName).append("=?").toString();
		sqlInsert = 
			new StringBuffer("insert into ")
			.append(tableName)
			.append("(")
			.append(idColumnName)
			.append(",")
			.append(valueColumnName)
			.append(") values(?,?)").toString();
		if(jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate();
			jdbcTemplate.setDataSource(dataSource);
			jdbcTemplate.afterPropertiesSet();
		}
		this.initAllowInterval = this.allowInterval;
	}
	
	public YqSimpleSequenceGenerator() {
	}
	
	public long nextSequence(String sequenceId) {
		OptimisticLockingFailureException lastException = null;
		for(int i = 0;i < 3;i++) {
			try{
				return nextSequence0(sequenceId);
			}catch(OptimisticLockingFailureException ex) {
				lastException = ex;
			}
		}
		throw lastException;
	}
	
	@Override
	public long nextSequence(String sequenceId, int interval) {
		OptimisticLockingFailureException lastException = null;
		for(int i = 0;i < 3;i++) {
			try{
				return nextSequence0(sequenceId,interval);
			}catch(OptimisticLockingFailureException ex) {
				lastException = ex;
			}
		}
		throw lastException;
	}

	protected boolean allowPreallocate(String sequenceId) {
		return allowPreallocate;
	}
	
	protected long nextSequence0(String sequenceId) {
		boolean allowPreallocate = allowPreallocate(sequenceId);
		if(allowPreallocate) {
			preallocatedSequences.putIfAbsent(sequenceId, new PreallocatedSequence());
			PreallocatedSequence seq = preallocatedSequences.get(sequenceId);
			synchronized(seq)  {
				long value = seq.nextValue();
				if(value > 0) {
//					System.out.println("preallocated :"+value);
					return value;
				}else {
					value = nextSequence0(sequenceId,allowInterval);
					seq.preallocate(value, value+allowInterval - 1);
					return value;
				}
			}
		}else {
			return nextSequence0(sequenceId,1);
		}
	}
	
	
	protected long nextSequence0(String sequenceId,int interval) {
		
		Long value = null;
		try{
			value = jdbcTemplate.queryForObject(sqlGet, Long.class, new Object[]{sequenceId});
		}catch(EmptyResultDataAccessException ex) {
			
		}
		long nextValue = 1;
		if(value == null) {
			jdbcTemplate.update(sqlInsert, new Object[]{sequenceId,nextValue+interval - 1});
		}else {
			nextValue = value.longValue() + 1;
			int effectedRows = jdbcTemplate.update(sqlUpdate, new Object[]{nextValue+interval - 1,sequenceId,value});
//			System.out.println("effected rows "+effectedRows);
//			System.out.println("new value "+(nextValue+interval));
			if(effectedRows <= 0) {
				throw new OptimisticLockingFailureException("");
			}
		}
		return nextValue;
	}
	
	public String nextTextSequence(String prefix,String suffix,int numberSize,String sequenceId) {
		StringBuffer buffer = new StringBuffer();
		if(prefix != null) {
			buffer.append(prefix);
		}
		long value = nextSequence(sequenceId);
		String numberText = String.valueOf(value);
		int currentSize = numberText.length();
		if(currentSize > numberSize) {
			buffer.append(numberText.substring(currentSize - numberSize));
		}else if(currentSize < numberSize) {
			for(int i = 0;i < numberSize - currentSize;i++) {
				buffer.append('0');
			}
			buffer.append(numberText);
		}else {
			buffer.append(numberText);
		}
		if(suffix != null) {
			buffer.append(suffix);
		}
		return buffer.toString();
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public String getIdColumnName() {
		return idColumnName;
	}



	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}



	public String getValueColumnName() {
		return valueColumnName;
	}



	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}



	public boolean isAllowPreallocate() {
		return allowPreallocate;
	}

	public void setAllowPreallocate(boolean allowPreallocate) {
		this.allowPreallocate = allowPreallocate;
	}

	public int getAllowInterval() {
		return allowInterval;
	}

	public void setAllowInterval(int allowInterval) {
		this.allowInterval = allowInterval;
	}

	public boolean isUseForUpdate() {
		return useForUpdate;
	}



	public void setUseForUpdate(boolean useForUpdate) {
		this.useForUpdate = useForUpdate;
	}

	public void setDataSource(DataSource ds ) {
		this.dataSource = ds;
		
	}
	
	
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}



	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	
	static class PreallocatedSequence {
		long startValue;
		long currentValue;
		long endValue;
		
		PreallocatedSequence() {}
		
		public void preallocate(long startValue,long endValue) {
			this.startValue = startValue;
			this.endValue = endValue;
			this.currentValue = this.startValue;
		}
		
		public void addSeq(long endValue) {
			
			this.endValue = endValue;
		}
		
		
		
		public boolean hasNext() {
			return currentValue < endValue;
		}
		
		public long nextValue() {
			return hasNext()?(++currentValue):-1;
		}
	}
    /**
     * 只有在 allowPreallocate = true 可以用
     */
	@Override
	public void addSequence(String sequenceId, int interval) {
 		boolean allowPreallocate = allowPreallocate(sequenceId);
		if(allowPreallocate) {
			preallocatedSequences.putIfAbsent(sequenceId, new PreallocatedSequence());
			PreallocatedSequence seq = preallocatedSequences.get(sequenceId);
			synchronized(seq)  {
				  long value = nextSequence0(sequenceId,interval);
				  if (seq.currentValue >0 ) {
					  seq.addSeq( value+interval - 1); 
				  } else {
					  seq.preallocate(value, value+interval - 1);
				  } 
 			} 
		}
		
	}
 	 
}
