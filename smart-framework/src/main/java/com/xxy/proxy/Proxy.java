package com.xxy.proxy;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 执行链式代理
     * 所谓链式代理，就是将多个代理通过一条链子串起来，一个个去执行，执行顺序取决于添加到链上的顺序
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
