package com.rnkrsoft.platform.definition.file;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by woate on 2019/1/26.
 */
public class InterfaceGlobalSettingTest {

    @Test
    public void testCreate() throws Exception {
        InterfaceGlobalSetting interfaceGlobalSetting = new InterfaceGlobalSetting();
        System.out.println(interfaceGlobalSetting);
    }

    @Test
    public void testLoad() throws Exception {
        InterfaceGlobalSetting interfaceGlobalSetting = InterfaceGlobalSetting.load(new File("./target/test-classes/global.json"));
        System.out.println(interfaceGlobalSetting);
    }
}