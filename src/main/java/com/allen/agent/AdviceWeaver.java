package com.allen.agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.objectweb.asm.commons.Method;

public class AdviceWeaver extends ClassVisitor implements Opcodes {

	public AdviceWeaver(int api, ClassVisitor cv) {
		super(api, cv);
	}
	
	@Override
	public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String desc,
            final String signature,
            final String[] exceptions) {
		final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		if (!name.equals("say")) {
			return mv;
		}
		System.out.println("invoke --- hello");
		return new AdviceAdapter(ASM5, new JSRInlinerAdapter(mv, access, name, desc, signature, exceptions), access, name, desc) {
			private final Type ASM_TYPE_OBJECT = Type.getType(Object.class);
            private final Type ASM_TYPE_OBJECT_ARRAY = Type.getType(Object[].class);
            private final Type ASM_TYPE_CLASS = Type.getType(Class.class);
            private final Type ASM_TYPE_INTEGER = Type.getType(Integer.class);
            private final Type ASM_TYPE_CLASS_LOADER = Type.getType(ClassLoader.class);
            private final Type ASM_TYPE_STRING = Type.getType(String.class);
            private final Type ASM_TYPE_THROWABLE = Type.getType(Throwable.class);
            private final Type ASM_TYPE_INT = Type.getType(int.class);
            private final Type ASM_TYPE_METHOD = Type.getType(java.lang.reflect.Method.class);
            private final Method ASM_METHOD_METHOD_INVOKE = Method.getMethod("Object invoke(Object,Object[])");
			@Override
			public void onMethodEnter() {
				getStatic(Type.getType("Lcom/allen/agent/Spy;"),"ON_BEFORE_METHOD",ASM_TYPE_METHOD);
				push((Type) null);
				loadArrayForBefore();
				invokeVirtual(ASM_TYPE_METHOD, ASM_METHOD_METHOD_INVOKE);
				pop();
			}
			
			public void loadArrayForBefore() {
				   push(5);
	               newArray(ASM_TYPE_OBJECT);
	                
	                dup();
	                push(0);
	                push(tranClassName("com.ding.Demo"));
	                arrayStore(ASM_TYPE_STRING);
	                
	                dup();
	                push(1);
	                push(name);
	                arrayStore(ASM_TYPE_STRING);
	                dup();
	                push(2);
	                push(desc);
	                arrayStore(ASM_TYPE_STRING);

	                dup();
	                push(3);
	                loadThisOrPushNullIfIsStatic();
	                arrayStore(ASM_TYPE_OBJECT);
	                
	                dup();
	                push(4);
	                loadArgArray();
	                arrayStore(ASM_TYPE_OBJECT_ARRAY);
	                
			}
			
			  /**
             * 加载this/null
             */
            private void loadThisOrPushNullIfIsStatic() {
                if (isStaticMethod()) {
                	push((Type) null);
                } else {
                    loadThis();
                }
            }
			
			public String tranClassName(String className) {
		        return className.replaceAll("/", ".");
		    }
			 private void loadClassLoader() {

	                if (this.isStaticMethod()) {

//	                    // fast enhance
//	                    if (GlobalOptions.isEnableFastEnhance) {
//	                        visitLdcInsn(Type.getType(String.format("L%s;", internalClassName)));
//	                        visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getClassLoader", "()Ljava/lang/ClassLoader;", false);
//	                    }

	                    // normal enhance
//	                    else {

	                    // 这里不得不用性能极差的Class.forName()来完成类的获取,因为有可能当前这个静态方法在执行的时候
	                    // 当前类并没有完成实例化,会引起JVM对class文件的合法性校验失败
	                    // 未来我可能会在这一块考虑性能优化,但对于当前而言,功能远远重要于性能,也就不打算折腾这么复杂了
	                    visitLdcInsn("com.ding.Demo");
	                    invokeStatic(ASM_TYPE_CLASS, Method.getMethod("Class forName(String)"));
	                    invokeVirtual(ASM_TYPE_CLASS, Method.getMethod("ClassLoader getClassLoader()"));
//	                    }

	                } else {
	                    loadThis();
	                    invokeVirtual(ASM_TYPE_OBJECT, Method.getMethod("Class getClass()"));
	                    invokeVirtual(ASM_TYPE_CLASS, Method.getMethod("ClassLoader getClassLoader()"));
	                }

	            }
			 private boolean isStaticMethod() {
                return (methodAccess & ACC_STATIC) != 0;
            }
		};
	}

}
