package com.rnkrsoft.platform.service.impl;

import com.rnkrsoft.platform.InterfaceContext;
import com.rnkrsoft.platform.InterfaceDefinition;
import com.rnkrsoft.platform.enums.InterfaceDirectionEnum;
import com.rnkrsoft.platform.enums.InterfaceStageEnum;
import com.rnkrsoft.platform.protocol.enums.InterfaceRspCode;
import com.rnkrsoft.platform.service.InterfaceEngine;

/**
 * Created by rnkrsoft.com on 2018/6/23.
 */
public class BootstrapInterfaceEngine implements InterfaceEngine {
    static final InterfaceEngine innerEngine = new InnerInterfaceEngine();
    static final InterfaceEngine outerSyncEngine = new OuterSyncInterfaceEngine();
    static final InterfaceEngine outerAsyncRequestInterfaceEngine = new OuterAsyncRequestInterfaceEngine();
    static final InterfaceEngine outerAsyncResponseInterfaceEngine = new OuterAsyncResponseInterfaceEngine();

    @Override
    public boolean execute(InterfaceContext context) {
        InterfaceDefinition interfaceDefinition = context.getInterfaceDefinition();
        if (interfaceDefinition.getInterfaceDirection() == InterfaceDirectionEnum.INNER) {
            return innerEngine.execute(context);
        } else if (interfaceDefinition.getInterfaceDirection() == InterfaceDirectionEnum.OUTER && !interfaceDefinition.isAsync()) {
            return outerSyncEngine.execute(context);
        } else if (interfaceDefinition.getInterfaceDirection() == InterfaceDirectionEnum.OUTER && interfaceDefinition.isAsync()) {
            if (interfaceDefinition.getInterfaceStage() == InterfaceStageEnum.REQUEST) {
                return outerAsyncRequestInterfaceEngine.execute(context);
            } else if (interfaceDefinition.getInterfaceStage() == InterfaceStageEnum.RESPONSE) {
                return outerAsyncResponseInterfaceEngine.execute(context);
            } else {
                context.getInnerResult().setEnum(InterfaceRspCode.UNSUPPORTED_DIRECTION);
                context.getOuterResult().setEnum(InterfaceRspCode.UNSUPPORTED_DIRECTION);
                return false;
            }
        } else {
            return false;
        }
    }
}
