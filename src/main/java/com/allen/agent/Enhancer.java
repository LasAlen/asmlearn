package com.allen.agent;

import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class Enhancer implements ClassFileTransformer{
	public Enhancer() {
		System.out.println("Enhancer new instance");
	}
	
	public byte[] transform(ClassLoader arg0, String arg1, Class<?> arg2, ProtectionDomain arg3, byte[] arg4)
			throws IllegalClassFormatException {
		final ClassReader cr;
		System.out.println(arg1);
		if (!arg1.equals("com/ding/Demo")) {
			return arg4;
		}
		System.out.println(arg1 + "over");
		try {
			System.out.println(arg4.length);
			cr = new ClassReader(arg4);
			System.out.println("invoke transform");
			final ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

			try {
				cr.accept(new AdviceWeaver(Opcodes.ASM5, cw),ClassReader.EXPAND_FRAMES);
				final byte[] enhanceClassByteArray = cw.toByteArray();
				FileOutputStream out = new FileOutputStream("D://demo.class");
				out.write(enhanceClassByteArray);
				out.flush();
				out.close();
				return enhanceClassByteArray;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return null;
	}

}
