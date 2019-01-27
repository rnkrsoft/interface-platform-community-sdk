package com.rnkrsoft.platform.definition.file;

import com.rnkrsoft.com.google.gson.Gson;
import com.rnkrsoft.com.google.gson.GsonBuilder;
import com.rnkrsoft.io.buffer.ByteBuf;
import lombok.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by woate on 2019/1/26.
 */
@Builder
@Setter
@Getter
public class InterfaceGlobalSetting implements Serializable {
    static Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    final Set<String> channels = new HashSet<String>(Arrays.asList("public"));

    public InterfaceGlobalSetting() {
    }


    @Override
    public String toString() {
        return GSON.toJson(this);
    }


    public static InterfaceGlobalSetting load(File globalFile) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(globalFile);
            ByteBuf byteBuf = ByteBuf.allocate(1024).autoExpand(true);
            byteBuf.read(fis);
            return GSON.fromJson(byteBuf.getString("UTF-8", byteBuf.readableLength()), InterfaceGlobalSetting.class);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }
}
