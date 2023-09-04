package com.aibank.framework.sentinellimit.test;

import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;
import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.stereotype.Service;

@Service
@BundleService(provider = "117000",service = "testService", version = "2020", interfaceType = TestApi.class)
public class TestService implements TestApi {

    @BundleService(service = "bApi", version = "2020", interfaceType = BApi.class, provider = "237001")
    private BApi bApi;

    @Override
    public String hello(BaseRequest body) {
        //  return "hi: " + name;
        return bApi.say(body);
    }

    @Override
    public String hello2(BaseRequest body) {
        //  return "hi: " + name;
        return bApi.say(body);
    }
}
