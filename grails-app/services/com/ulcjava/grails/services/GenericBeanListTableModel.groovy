package com.ulcjava.grails.services

import com.ulcjava.base.application.table.AbstractTableModel
import org.codehaus.groovy.grails.commons.ApplicationHolder

class GenericBeanListTableModel extends AbstractTableModel {
    List beans
    List domainClassProperties
    Class clazz

    GenericBeanListTableModel(domainClass) {
        def app = ApplicationHolder.application
        def name = domainClass.fullName
        clazz = app.getClassForName(name)
        beans = clazz.list()
        def props = domainClass.getProperties()
        def persistentProperties = props.findAll {
            it.persistent
        }
        this.domainClassProperties = persistentProperties.name - ['password', 'passwd']
    }

    public int getColumnCount() {
        domainClassProperties.size()
    }

    public int getRowCount() {
        beans.size()
    }

    public Object getValueAt(int row, int column) {
        String propname = getColumnName(column)
        def domainObject = beans[row]
        domainObject = domainObject.class.get(domainObject.id) // refetch potentially detached object
        return String.valueOf(domainObject[propname])
    }

    public String getColumnName(int column) {
        domainClassProperties[column]
    }


}