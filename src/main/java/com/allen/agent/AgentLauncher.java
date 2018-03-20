package com.allen.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class AgentLauncher {
	public static void premain(String args, Instrumentation inst) {
		try {
			main(args, inst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void agentmain(String args, Instrumentation inst) {
		try {
			main(args, inst);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 private static synchronized void main(final String args, final Instrumentation inst) throws IOException {
		 System.out.println("invoke");
		 inst.appendToBootstrapClassLoaderSearch(
				 new JarFile("G:\\eclipse\\workspace\\allen-agent\\target\\allen-agent.jar")
		 );

		 inst.addTransformer(new
				 Enhancer(), true);
		 try {
			 Spy.initForAgentLauncher( MethodInvoke.class.getMethod("methodOnBegin",
                     String.class,
                     String.class,
                     String.class,
                     Object.class,
                     Object[].class));
		 } catch (NoSuchMethodException e) {
			 e.printStackTrace();
		 }
		 Class<?>[] classes = inst.getAllLoadedClasses();
		 for (Class<?> clazz : classes) {
			 //System.out.println(clazz.getName());
			if (clazz.getName().equals("com.ding.Demo")) {
				try {
					inst.retransformClasses(clazz);
				} catch (UnmodifiableClassException e) {
					e.printStackTrace();
				}
			}
		 }
		 System.out.println("invoke over");
	 }
}
