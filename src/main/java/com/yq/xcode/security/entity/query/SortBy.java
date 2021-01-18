package com.yq.xcode.security.entity.query;

import java.io.Serializable;

public class SortBy implements Serializable{

	public static enum Direction {
		ASC,
		DESC
	}
	private String name;
	
	private Direction direction = Direction.ASC;
	
	public SortBy() {}
	
	public SortBy(String name) {
		this.name = name;
	}
	
	public SortBy(String name,Direction direction) {
		this.name = name;
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	
}
