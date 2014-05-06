package com.aspectj.analysis;

public class ValueChangePoint {
		public String methodName;
		public String value;
		
		public ValueChangePoint(String methodName, String value, String newValue)
		{
			this.methodName = methodName;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
}
