package com.aibank.framework.sentinellimit.test;

import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;

public interface TestApi {
    String hello(BaseRequest body);

    String hello2(BaseRequest body);
}
