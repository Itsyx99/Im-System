package com.itsyx.im.tcp.reciver.process;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public class ProcessFactory {

    private static BaseProcess defaultProcess;

    static {
        defaultProcess = new BaseProcess() {
            @Override
            public void processBefore() {

            }

            @Override
            public void processAfter() {

            }
        };
    }

    public static BaseProcess getMessageProcess(Integer command) {
        return defaultProcess;
    }

}
