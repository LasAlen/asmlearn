package com.allen.agent;

import java.lang.reflect.Method;

public class MethodInvoke {

	public static void methodOnBegin(
            String className,
            String methodName,
            String methodDesc,
            Object thisParam,
            Object[] params) {
		System.out.println(className);
		System.out.println(methodName);
		System.out.println(methodDesc);
		System.out.println(thisParam);
	}
}

