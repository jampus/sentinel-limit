
package com.aibank.framework.sentinellimit.flow.interceptor;


public class DefaultBlockHandler implements BlockHandler {
    @Override
    public Object handle() {
        return "blocked";
    }
}
