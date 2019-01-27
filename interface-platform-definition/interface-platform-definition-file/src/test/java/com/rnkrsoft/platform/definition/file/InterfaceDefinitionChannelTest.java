package com.rnkrsoft.platform.definition.file;

import org.junit.Test;

import java.io.File;

/**
 * Created by woate on 2019/1/26.
 */
public class InterfaceDefinitionChannelTest {

    @Test
    public void testToString() throws Exception {
        InterfaceDefinitionChannel interfaceDefinitionChannel = new InterfaceDefinitionChannel();
        interfaceDefinitionChannel.setChannel("test-channel");
        interfaceDefinitionChannel.setDescription("测试通道");
        interfaceDefinitionChannel.setEnabled(true);
        for (int i = 0; i < 150; i++) {
            interfaceDefinitionChannel.getInterfaces().add(InterfaceDefinitionMetadata.builder()
                            .channel("test-channel")
                            .txNo(Integer.toString(i))
                            .version("1")
                            .interfaceDesc("hello" + Integer.toString(i))
                            .serviceClassName("com.rnkrsoft.platform.demo.HelloSerive")
                            .methodName("hello")
                            .useTokenAsPassword(false)
                            .validateToken(true)
                            .decryptAlgorithm("AES")
                            .encryptAlgorithm("AES")
                            .signAlgorithm("SHA512")
                            .verifyAlgorithm("SHA512")
                            .build()
            );
        }
        System.out.println(interfaceDefinitionChannel);
    }

    @Test
    public void testLoad() throws Exception {
        InterfaceDefinitionChannel interfaceDefinitionChannel = InterfaceDefinitionChannel.load(new File("./target/test-classes/test-channel.json"));
        System.out.println(interfaceDefinitionChannel);
    }
}