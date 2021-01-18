package com.yq.xcode.security.crypto.coder;

public interface DataCoder<T,E> {

	public E encode(T src);
	
	public T decode(E src);
}
