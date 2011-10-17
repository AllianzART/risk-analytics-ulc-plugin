package com.ulcjava.container.grails;

import com.ulcjava.base.application.AbstractApplication;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCRootPane;

public class UlcApplication extends AbstractApplication {

    ULCRootPane fRootPane;
    UlcViewFactory fViewFactory;

    public void start() {
        String viewFactoryClassName = ClientContext.getUserParameter("ViewFactory");
        try {
            Class factoryClass = Class.forName(viewFactoryClassName);
            fViewFactory = (UlcViewFactory) factoryClass.newInstance();
            ULCRootPane rootPane = fViewFactory.create();
            if (rootPane == null) {
                throw new IllegalStateException("failed to create rootPane with factory '" + viewFactoryClassName + "'");
            }
            fRootPane = rootPane;
            fRootPane.setVisible(true);
        } catch (Exception e) {
            throw new IllegalStateException("failed to instantiate factory '" + viewFactoryClassName + "'", e);
        }
    }

    public void stop() {
        fViewFactory.stop();
        fRootPane.setVisible(false);
        super.stop(); // is currently empty, but who knows....
    }

}
