/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.cluster.loadbalance;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RoundRobinLoadBalanceTest
 * @author liuchao
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RandomWeightLoadBalanceTest extends LoadBalanceTest{
    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        
        invocation = EasyMock.createMock(Invocation.class);
        EasyMock.expect(invocation.getMethodName()).andReturn("method1").anyTimes();
        
        invoker1 = EasyMock.createMock(Invoker.class);
        invoker2 = EasyMock.createMock(Invoker.class);
        invoker3 = EasyMock.createMock(Invoker.class);
        invoker4 = EasyMock.createMock(Invoker.class);
        invoker5 = EasyMock.createMock(Invoker.class);
        
        URL url = URL.valueOf("test://127.0.0.1/DemoService?weight=0");
        
        EasyMock.expect(invoker1.isAvailable()).andReturn(true).anyTimes();
        EasyMock.expect(invoker1.getInterface()).andReturn(LoadBalanceTest.class).anyTimes();
        EasyMock.expect(invoker1.getUrl()).andReturn(url).anyTimes();

        url = URL.valueOf("test://127.0.0.1/DemoService?weight=100");
        EasyMock.expect(invoker2.isAvailable()).andReturn(true).anyTimes();
        EasyMock.expect(invoker2.getInterface()).andReturn(LoadBalanceTest.class).anyTimes();
        EasyMock.expect(invoker2.getUrl()).andReturn(url).anyTimes();

        EasyMock.replay(invocation,invoker1,invoker2);
        
        invokers.add(invoker1);
        invokers.add(invoker2);
    }

    @Test
    public void testRandomLoadBalance_select() {
        int runs = 1000;
        Map<Invoker,AtomicLong> counter = getInvokeCounter(runs,RandomLoadBalance.NAME);
        for (Invoker minvoker :counter.keySet() ){
            Long count = counter.get(minvoker).get();
            System.out.println(minvoker.getUrl().toParameterString() + " " + count);
        }
    }

}