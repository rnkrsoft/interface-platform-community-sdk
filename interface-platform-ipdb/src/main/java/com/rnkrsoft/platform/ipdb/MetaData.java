package com.rnkrsoft.platform.ipdb;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @copyright IPIP.net
 * {"build":1535696240,"ip_version":1,"languages":{"CN":0},"node_count":385083,"total_size":3117287,"fields":["country_name","region_name","city_name"]}
 */
@Data
public class MetaData implements Serializable{
    @SerializedName("build")
    public int build;
    @SerializedName("ip_version")
    public int ipVersion;
    @SerializedName("node_count")
    public int nodeCount;
    @SerializedName("languages")
    public Map<String, Integer> languages;
    @SerializedName("fields")
    public String[] fields;
    @SerializedName("total_size")
    public int totalSize;
}