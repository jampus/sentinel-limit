package com.aibank.framework.sentinellimit.test;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.stereotype.Service;

@Service
@BundleService(service = "testService", version = "2020", interfaceType = TestApi.class)
public class TestService implements TestApi {

    @BundleService(service = "bApi", version = "2020", interfaceType = BApi.class,provider = "237001")
    private BApi bApi;

    @Override
    public String hello(String name) {
      //  return "hi: " + name;
        return bApi.say(name);
    }
}
