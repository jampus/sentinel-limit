package com.aibank.framework.sentinellimit.test;

import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/doRequest/test1")
    public String doRequest1(@RequestBody BaseRequest body) {
        TestApi testApi = applicationContext.getBean(TestApi.class);
        return testApi.hello(body);
    }

    @RequestMapping("/doRequest/test2")
    public String doRequest2(@RequestBody BaseRequest body) {
        TestApi testApi = applicationContext.getBean(TestApi.class);
        return testApi.hello2(body);
    }
}
