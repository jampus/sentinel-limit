package com.aibank.framework.sentinellimit.flow.interceptor.limit;

import com.baidu.ub.msoa.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.rpc.interceptor.ProviderInterceptor;

public class InboundOriginLimitation implements ProviderInterceptor {
    @Override
    public void beforeInvoke(RPCRequest rpcRequest) {

    }

    @Override
    public void afterInvoke(RPCRequest rpcRequest, RPCResponse rpcResponse) {

    }
}
