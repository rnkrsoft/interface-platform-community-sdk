package com.rnkrsoft.platform.definition.file;

import com.rnkrsoft.com.google.gson.Gson;
import com.rnkrsoft.com.google.gson.GsonBuilder;
import com.rnkrsoft.io.buffer.ByteBuf;
import lombok.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InterfaceDefinitionChannel implements Serializable {
    static Gson GSON=new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    String channelNo;
    String channelName;
    String password = "1234567890";
    String keyVector = "1234567890654321";
    boolean enabled;
    final Set<InterfaceDefinitionMetadata> interfaces = new HashSet<InterfaceDefinitionMetadata>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterfaceDefinitionChannel that = (InterfaceDefinitionChannel) o;

        return channelNo.equals(that.channelNo);

    }

    @Override
    public int hashCode() {
        return channelNo.hashCode();
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public static InterfaceDefinitionChannel load(File channelFile) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(channelFile);
            ByteBuf byteBuf = ByteBuf.allocate(1024).autoExpand(true);
            byteBuf.read(fis);
            return GSON.fromJson(byteBuf.getString("UTF-8", byteBuf.readableLength()), InterfaceDefinitionChannel.class);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }
}