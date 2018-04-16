// ITestService.aidl
package com.example.binder.bindertest;

import com.example.binder.bindertest.ISystemTimeListener;

/** Example service interface */
interface ITestService {
    /** Request the process ID of this service, to do evil things with it. */
    int getPid();

    /** Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    oneway void registerForTimeUpdates(ISystemTimeListener listener);
}