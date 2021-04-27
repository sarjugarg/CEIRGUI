package com.gl.ceir.config.factory;

public interface CustomerCareRepo<T> {
	public T getByTxnId(String txnId);
}
