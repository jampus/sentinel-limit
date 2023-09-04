package com.aibank.framework.sentinellimit.flow.util;

import com.aibank.framework.sentinellimit.domain.LimitConstants;
import com.aibank.framework.sentinellimit.flow.domain.BaseRequest;
import com.baidu.ub.msoa.utils.StringUtil;

public class RequestContentUtils {

    public static String getOriginProviderId(BaseRequest baseRequest) {
        String sourceServiceId = baseRequest.getHead().getSourceServiceId();
        if (!StringUtil.isBlank(sourceServiceId) && sourceServiceId.length() >= 6 && checkFirstSixDigits(sourceServiceId)) {
            return sourceServiceId.substring(0, 6);
        }
        return LimitConstants.DEFAULT_ORIGIN;
    }

    public static boolean checkFirstSixDigits(String str) {
        // 使用正则表达式检查前6位是否是数字
        return str.substring(0, 6).matches("\\d{6}");
    }

}
