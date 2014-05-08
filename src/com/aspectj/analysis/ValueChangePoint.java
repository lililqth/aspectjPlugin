package com.aspectj.analysis;

public class ValueChangePoint {
		public String methodName;
		public String oldValue;
		public String newValue;
		
		public ValueChangePoint(String methodName, String oldValue, String newValue)
		{
			this.methodName = methodName;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
}
