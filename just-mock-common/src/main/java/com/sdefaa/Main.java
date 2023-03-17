package com.sdefaa;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author Julius Wong
 * <p>
 * test
 * <p>
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        VirtualMachine.list().forEach(virtualMachineDescriptor -> {
            System.out.println(virtualMachineDescriptor.displayName());
            System.out.println(virtualMachineDescriptor.provider().type());
            System.out.println(virtualMachineDescriptor.provider().name());
            System.out.println(virtualMachineDescriptor.id());
//            try {
//                VirtualMachine vm = VirtualMachine.attach("44348");
//            } catch (AttachNotSupportedException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
}
