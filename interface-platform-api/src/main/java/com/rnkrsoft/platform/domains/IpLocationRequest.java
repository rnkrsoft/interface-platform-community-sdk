package com.rnkrsoft.platform.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.web.doc.annotation.ApidocElement;
import java.io.Serializable;

/**
 * Created by rnkrsoft.com on 2018/12/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IpLocationRequest implements Serializable{
    @ApidocElement("IP地址信息")
    String address;
}
