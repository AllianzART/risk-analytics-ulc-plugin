package com.ulcjava.container.servlet.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;


/**
 * This class is only to allow access to the servletConfig for the UlcController.
 * Make sure that this Servlet is loaded at server startup! (see load-at-startup in UlcGrailsPlugin.groovy doWithWebDescriptor)
 *
 * @author Dierk.Koenig@canoo.com
 */
public class ServletConfigProviderServlet extends ServletContainerAdapter {

    public static ServletConfig sServletConfig;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        sServletConfig = servletConfig;
    }

}
