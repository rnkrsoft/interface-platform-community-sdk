package com.rnkrsoft.platform.mongo.entity;

import com.rnkrsoft.framework.orm.PrimaryKey;
import com.rnkrsoft.framework.orm.PrimaryKeyStrategy;
import com.rnkrsoft.framework.orm.ValueMode;
import com.rnkrsoft.framework.orm.mongo.MongoColumn;
import com.rnkrsoft.framework.orm.mongo.MongoTable;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rnkrsoft.com on 2018/6/26.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MongoTable(name = "INTERFACE_RESPONSE")
public class MongoInterfaceResponseEntity implements Serializable{
    @PrimaryKey(strategy = PrimaryKeyStrategy.UUID)
    @MongoColumn(name = "_id", nullable = false, valueMode = ValueMode.EQUAL)
    String responseNo;

    @MongoColumn(name = "RSP_TYPE", nullable = false)
    Integer rspType;

    @MongoColumn(name = "INNER_MESSAGE", nullable = true)
    String innerMessage;

    @MongoColumn(name = "OUTER_MESSAGE", nullable = true)
    String outerMessage;

    @MongoColumn(name = "INNER_RSP_CODE", nullable = true)
    String innerRspCode;

    @MongoColumn(name = "INNER_RSP_DESC", nullable = true)
    String innerRspDesc;

    @MongoColumn(name = "OUTER_RSP_CODE", nullable = true)
    String outerRspCode;

    @MongoColumn(name = "OUTER_RSP_DESC", nullable = true)
    String outerRspDesc;

    @MongoColumn(name = "CAUSE_STACK_TRACE", nullable = true)
    String causeStackTrace;

    @MongoColumn(name = "CAUSE_MESSAGE", nullable = true)
    String causeMessage;

    @MongoColumn(name = "REQUEST_NO", nullable = false, valueMode = ValueMode.EQUAL)
    String requestNo;

    @MongoColumn(name = "CREATE_DATE", nullable = false)
    Date createDate;

    @MongoColumn(name = "LAST_UPDATE_DATE", nullable = false)
    Date lastUpdateDate;
}
