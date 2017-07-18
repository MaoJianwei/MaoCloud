package com.maojianwei.MaoCloud.cli.base;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;


/**
 * Created by mao on 2016/10/16.
 */
public class DefaultServiceDirectory {

    public static <T> T getService(Class<T> serviceClass){
        BundleContext bc = FrameworkUtil.getBundle(serviceClass).getBundleContext();
        if(bc != null){
            ServiceReference<T> reference = bc.getServiceReference(serviceClass);
            if(reference != null){
                T impl = bc.getService(reference);
                if(impl != null){
                    return impl;
                }
            }
        }
        throw new ServiceNotFoundException("Service " + serviceClass.getName() + " not found");
    }
}
