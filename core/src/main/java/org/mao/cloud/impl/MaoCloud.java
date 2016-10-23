/*
 * Copyright (c) 2016. Mao Jianwei
 *
 * MaoCloud deployed in Beijing University of Posts and Telecommunications(BUPT), China.
 *
 * All Rights Reserved.
 */

package org.mao.cloud.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.intf.MaoCloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mao on 5/2/16.
 */
@Component(immediate = true) //TODO - modify to true
@Service
public class MaoCloud implements MaoCloudService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static String VERSION = "v0.6";

    @Activate
    protected void activate(){
        log.info("MaoCloud init ...");
        log.info("MaoCloud {} , Welcome aboard!", VERSION);
    }

    @Deactivate
    protected void deactivate(){
        log.info("MaoCloud destroy ...");
        log.info("MaoCloud {} , Good day!", VERSION);
    }

    public void debug(){
        return;
    }

}
