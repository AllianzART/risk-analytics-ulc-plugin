package com.ulcjava.container.grails;

import com.ulcjava.applicationframework.application.Application;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCRootPane;

public class UlcApplication extends Application {

    ULCRootPane fRootPane;
    UlcViewFactory fViewFactory;

    protected void startup() {
        String viewFactoryClassName = ClientContext.getUserParameter("ViewFactory");
        try {
            Class factoryClass = Class.forName(viewFactoryClassName);
            fViewFactory = (UlcViewFactory) factoryClass.newInstance();
            ULCRootPane rootPane = fViewFactory.create(getContext());
            if (rootPane == null) {
                throw new IllegalStateException("failed to create rootPane with factory '" + viewFactoryClassName + "'");
            }
            fRootPane = rootPane;
            fRootPane.setVisible(true);
        } catch (Exception e) {
            throw new IllegalStateException("failed to instantiate factory '" + viewFactoryClassName + "'", e);
        }
    }


    protected void shutdown() {
        fViewFactory.stop();
        fRootPane.setVisible(false);
        super.shutdown();
    }

    @Override
    protected void shutdown(Throwable reason) {
        fViewFactory.stop(reason);
        fRootPane.setVisible(false);
        super.shutdown(reason);
    }
}
