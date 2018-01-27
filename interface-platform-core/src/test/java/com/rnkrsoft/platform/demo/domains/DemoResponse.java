package com.rnkrsoft.platform.demo.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.web.doc.AbstractResponse;

/**
 * Created by Administrator on 2018/6/19.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DemoResponse extends AbstractResponse{
    Integer age;
}
