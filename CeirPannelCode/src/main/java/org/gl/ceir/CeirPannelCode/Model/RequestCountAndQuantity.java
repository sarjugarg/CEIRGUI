package org.gl.ceir.CeirPannelCode.Model;

public class RequestCountAndQuantity {
	private long count;
	private long quantity;

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "RequestCountAndQuantity [count=" + count + ", quantity=" + quantity + "]";
	}
	
	

}
