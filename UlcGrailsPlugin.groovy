class UlcGrailsPlugin {
    def version = "ria-suite-2013"
    def dependsOn = [:]

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }
    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }
    def doWithWebDescriptor = { webXml ->

        def servlets = webXml.'servlet'
        def servletElement = servlets[servlets.size() - 1]

        servletElement + {
            'servlet' {
                'servlet-name'('ServletConfigProvider')
                'servlet-class'('com.ulcjava.container.servlet.server.ServletConfigProviderServlet')
                'init-param' {                          // will be relayed via the servletConfig
                    'param-name'('application-class')
                    'param-value'('com.ulcjava.container.grails.UlcApplication')
                }
                'init-param' {
                    'param-name'('log-level')
                    'param-value'('WARNING')
                }
                'load-on-startup'('0') // make sure to load before com.ulcjava.grails.controller.UlcServerEndpointController
            }
        }

        servletElement = servlets[servlets.size() - 1]

        servletElement + {
            'servlet' {
                'servlet-name'('JnlpDownloadServlet')
                'servlet-class'('jnlp.sample.servlet.JnlpDownloadServlet')
            }
        }

        def mappings = webXml.'servlet-mapping'
        def mappingElement = mappings[mappings.size() - 1]
        mappingElement + {
            'servlet-mapping' {
                'servlet-name'('ServletConfigProvider')
                'url-pattern'('/nevercalled.really')
            }
        }
        mappingElement + {
            'servlet-mapping' {
                'servlet-name'('JnlpDownloadServlet')
                'url-pattern'('*.jnlp')
            }
        }

        def mimeMapping = webXml.'mime-mapping'
        mimeMapping + {
            'mime-mapping' {
                'extension'('jnlp')
                'mime-type'('application/x-java-jnlp-file')
            }
        }
    }
    def onChange = { event ->
        // TODO Implement code that is executed when this class plugin class is changed
        // the event contains: event.application and event.applicationContext objects
    }
    def onApplicationChange = { event ->
        // TODO Implement code that is executed when any class in a GrailsApplication changes
        // the event contain: event.source, event.application and event.applicationContext objects
    }
}
