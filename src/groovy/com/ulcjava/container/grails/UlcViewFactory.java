package com.ulcjava.container.grails;

import com.ulcjava.applicationframework.application.ApplicationContext;
import com.ulcjava.base.application.ULCRootPane;

public interface UlcViewFactory {

    public ULCRootPane create(ApplicationContext context);

    public void stop();
}
