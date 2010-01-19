package com.ulcjava.container.grails;

import com.ulcjava.base.application.AbstractApplication;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCRootPane;

public class UlcApplication extends AbstractApplication {
    ULCRootPane fRootPane;

    public void start() {


        String viewFactoryClassName = ClientContext.getUserParameter("ViewFactory");
        ULCRootPane rootPane = createApplicationView(viewFactoryClassName);
        if (rootPane == null) {
            throw new IllegalStateException("failed to create rootPane with factory '" + viewFactoryClassName + "'");
        }
        fRootPane = rootPane;
        fRootPane.setVisible(true);
    }

    private ULCRootPane createApplicationView(String factoryClassName) {
        try {
            Class factoryClass = Class.forName(factoryClassName);
            UlcViewFactory viewFactory = (UlcViewFactory) factoryClass.newInstance();
            ULCRootPane rootPane = viewFactory.create();
            return rootPane;
        } catch (Exception e) {
            throw new IllegalStateException("failed to instantiate factory '" + factoryClassName + "'", e);
        }
    }

    public void stop() {
        // todo: anything else to do here?

        fRootPane.setVisible(false);
        super.stop(); // is currently empty, but who knows....
    }

}
