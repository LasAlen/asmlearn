package com.allen.attach;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.allen.agent.AdviceWeaver;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class AgentAttach {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach("15052");
        vm.loadAgent("G:\\eclipse\\workspace\\allen-agent\\target\\allen-agent.jar");
        //vm.loadAgent("D:\\allen-agent.jar");
    }

    @Test
    public void test() throws IOException {
        try {
            FileInputStream in = new FileInputStream("G:\\eclipse\\workspace\\allen-agent\\target\\classes\\com\\ding\\Demo.class");
            final ClassReader cr = new ClassReader(in);
            System.out.println("invoke transform");
            final ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            try {
                cr.accept(new AdviceWeaver(Opcodes.ASM5, cw), ClassReader.EXPAND_FRAMES);
                final byte[] enhanceClassByteArray = cw.toByteArray();
                FileOutputStream out = new FileOutputStream("G://self//1.class");
                out.write(enhanceClassByteArray);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
