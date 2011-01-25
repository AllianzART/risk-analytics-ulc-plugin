package com.ulcjava.grails.controller

import com.ulcjava.container.servlet.server.ServletConfigProviderServlet
import com.ulcjava.container.servlet.server.ServletContainerAdapterHelper
import org.codehaus.groovy.grails.support.PersistenceContextInterceptor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class UlcServerEndpointController implements ApplicationContextAware {

    ServletContainerAdapterHelper servletContainerAdapterHelper
    ApplicationContext appCtx

    public void setApplicationContext(ApplicationContext applicationContext) {
        appCtx = applicationContext
    }

    def gate = {

        def config = ServletConfigProviderServlet.sServletConfig
        log.info " relaying to ulc with parameters: $request \n $response \n $config "


        if (!servletContainerAdapterHelper) {
            servletContainerAdapterHelper = new ServletContainerAdapterHelper(config)
        }

        boolean handled = servletContainerAdapterHelper.service(request, response, config)

        if (!handled) {
            log.warn "request not handled by ServletContainerAdapterHelper"
        }
        response.flushBuffer()
    }

}