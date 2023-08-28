package com.aibank.framework.sentinellimit.test;

import com.baidu.ub.msoa.container.support.governance.annotation.BundleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/doRequest/{name}")
    public String doRequest(@PathVariable String name) {
        TestApi testApi = applicationContext.getBean(TestApi.class);
        return testApi.hello(name);
    }
}
