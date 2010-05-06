package com.ulcjava.grails.taglib
class UlcTagLib {

    private static plugInDir = "plugins/ulc-2008-u4"
    private static plugInKey = "plugin"

    static namespace = 'ulc'

    final String APPLET_ERROR_MESSAGE = 'Your browser does not support JDK 1.5 or higher for applets.'

    final Map PARAMS = [
            'java_codebase': ".",
            'keep-alive-interval': "900",
            'log-level': "WARNING",
            'java_code': 'com.ulcjava.environment.applet.client.DefaultAppletLauncher.class'
    ]

    def applet = {attrs, body ->

        PARAMS.'url-string' = request.getScheme() +
                "://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath() +
                "/ulcServerEndpoint/gate";

        def archive = []

        //the virtual path to the requested URI (incl. file / action name)
        //like GrailsAppName/application.gsp or GrailsAppName/plugins/myplugin/application.gsp
        String contextPath = request.getRequestURI()
        //strip file / action name, results in the virtual location of the application or plugin
        contextPath = contextPath.substring(0, contextPath.lastIndexOf('/'))

        String appPluginDir = ""
        //If the grails app which uses the ulc plugin is itself a plugin..
        if(contextPath.contains("plugins")) {
            appPluginDir = "plugins/" + contextPath.substring(contextPath.lastIndexOf('/'))
        }
        //real path to the web-app directory
        def realPath = servletContext.getRealPath('/')

        //use absolute path to jar files to get all the file names
        File serverContextPath = new File(realPath)
        File plugInLibDir = new File(serverContextPath, "${plugInDir}/lib")
        File appLibDir = new File(serverContextPath, "${appPluginDir}/lib")

        //use virtual paths to create applet parameters
        for (fileName in plugInLibDir.list()) {
            archive << "${contextPath}/${plugInDir}/lib/${fileName}"
        }
        if (!attrs[plugInKey]) {
            for (fileName in appLibDir.list()) {
                archive << "${contextPath}/lib/${fileName}"
            }
        }

        PARAMS.java_archive = archive.join(',')

        def userParams = [:]
        for (attr in attrs) {
            if (attr.key.startsWith('userParameter_')) {
                userParams[attr.key - 'userParameter_'] = attr.value
            }
        }
        if (userParams) PARAMS.'user-parameter-names' = userParams.keySet().join(',')

        for (key in userParams.keySet()) attrs.remove('userParameter_' + key)

        PARAMS.putAll attrs
        PARAMS.putAll userParams

        def userAgent = request.getHeader("User-Agent")
        if (userAgent.contains("Mac") && (userAgent.contains("MSIE") || userAgent.contains("Safari"))) {
            renderMSIEorSafariOnMac()
        } else {
            renderWindowsOrElse()
        }
    }


    private def renderMSIEorSafariOnMac() {
        out << """
        <APPLET WIDTH="100%" HEIGHT="100%"
                CODEBASE = "${PARAMS.java_codebase}"
                CODE     = "${PARAMS.java_code}"
                ARCHIVE  = "${PARAMS.java_archive}">
"""
        renderParamsAsNestedElements()
        out << """
            ${APPLET_ERROR_MESSAGE}
        </APPLET>
"""
    }

    private def renderWindowsOrElse() {
        PARAMS.type = "application/x-java-applet;version=1.5"
        out << """
            <OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
                width="100%" height="100%"
                codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_5_0-windows-i586.cab#Version=1,5,0,0">
"""
        renderParamsAsNestedElements()
        out << """
            <COMMENT>
                <EMBED  width="100%" height="100%"
                        pluginspage="http://java.sun.com/products/plugin/"
"""
        renderParamsAsAttributes()
        out << """
                >
                <NOEMBED>
                    ${APPLET_ERROR_MESSAGE}
                </NOEMBED>
            </COMMENT>
        </OBJECT>
"""
    }

    private def renderParamsAsNestedElements() {
        for (param in PARAMS) {
            out << """
                <PARAM NAME="${param.key}" VALUE="${param.value}">  """
        }
    }

    private def renderParamsAsAttributes() {
        for (param in PARAMS) {
            out << """
                        ${param.key}="${param.value}"  """
        }
    }
}