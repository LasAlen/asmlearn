package com.allen.agent;

import java.lang.reflect.Method;

public class Spy {
    public static volatile Method ON_BEFORE_METHOD;

    /*
     * 用于启动线程初始化
     */
    public static void initForAgentLauncher(
            Method onBeforeMethod
            ) {
        ON_BEFORE_METHOD = onBeforeMethod;
    }
}
