package com.ulcjava.grails.services

import com.ulcjava.base.application.event.ActionEvent
import com.ulcjava.base.application.event.IActionListener
import com.ulcjava.base.application.event.IListSelectionListener
import com.ulcjava.base.application.event.ListSelectionEvent
import com.ulcjava.container.grails.UlcViewFactory
import com.ulcjava.environment.applet.application.ULCAppletPane
import org.codehaus.groovy.grails.commons.ApplicationHolder
import com.ulcjava.base.application.ULCTable
import com.ulcjava.base.application.ULCContainer
import com.ulcjava.base.application.ULCRootPane
import com.ulcjava.applicationframework.application.ApplicationContext
import com.ulcjava.base.application.ULCBorderLayoutPane
import com.ulcjava.base.application.BorderFactory
import com.ulcjava.base.application.ULCComponent
import com.ulcjava.base.application.ULCComboBox
import com.ulcjava.base.application.ULCBoxPane
import com.ulcjava.base.application.ULCFiller
import com.ulcjava.base.application.ULCListSelectionModel
import com.ulcjava.base.application.ULCScrollPane
import com.ulcjava.base.application.ULCLabel
import com.ulcjava.base.application.ULCTextField

public class UlcHomeViewFactory implements UlcViewFactory {

    def domainClasses
    def domainSelectionBox
    ULCTable domainTable
    ULCContainer detailPanel

    public ULCRootPane create(ApplicationContext context) {

        ULCAppletPane appletPane = ULCAppletPane.getInstance();
        ULCBorderLayoutPane pane = new ULCBorderLayoutPane(10, 10);
        pane.setBorder(BorderFactory.createEmptyBorder(15, 15, 50, 15))
        pane.add(createDomainSelection(), ULCBorderLayoutPane.NORTH)
        pane.add(createDomainList(), ULCBorderLayoutPane.CENTER)
        pane.add(createDetailPanel(), ULCBorderLayoutPane.SOUTH)

        updateDomainList()
        appletPane.add(pane)
        return appletPane;
    }

    public ULCComponent createDomainSelection() {
        domainClasses = ApplicationHolder.application.domainClasses
        domainSelectionBox = new ULCComboBox(domainClasses.fullName)
        domainSelectionBox.addActionListener(new DomainSelectionChangeHandler(this))

        ULCBoxPane holder = new ULCBoxPane(2, 1)
        holder.add(ULCBoxPane.BOX_LEFT_CENTER, domainSelectionBox)
        holder.add(ULCBoxPane.BOX_EXPAND_CENTER, new ULCFiller())

        return holder
    }

    ULCComponent createDomainList() {
        domainTable = new ULCTable()
        domainTable.setSelectionMode(ULCListSelectionModel.SINGLE_SELECTION)
        domainTable.selectionModel.addListSelectionListener(new BeanSelectionChangeHandler(this))
        return new ULCScrollPane(domainTable)
    }

    ULCComponent createDetailPanel() {
        detailPanel = new ULCBoxPane(2, 1, 15, 10)
    }

    void refreshDetailPanel() {
        detailPanel.removeAll()

        ULCLabel headerLabel = new ULCLabel()
        headerLabel.font = headerLabel.font.deriveFont((headerLabel.font.size + 4) as float)
        detailPanel.add(2 as int, ULCBoxPane.BOX_LEFT_CENTER, headerLabel)

        detailPanel.add(ULCBoxPane.BOX_RIGHT_CENTER, new ULCLabel('id :'))
        ULCLabel idLabel = new ULCLabel()
        idLabel.font = new ULCTextField().font
        detailPanel.add(ULCBoxPane.BOX_EXPAND_CENTER, idLabel)
        for (propName in domainTable.model.domainClassProperties) {
            detailPanel.add(ULCBoxPane.BOX_RIGHT_CENTER, new ULCLabel(propName + ' :'))
            ULCTextField field = new ULCTextField()
            field.editable = false
            detailPanel.add(ULCBoxPane.BOX_EXPAND_CENTER, field)
        }
    }

    void updateDetailValues() {
        int selectionIndex = domainTable.getSelectedRow()
        def selectedBean = (selectionIndex < 0) ? null : domainTable.model.beans[selectionIndex]

        def componentIndex = 0
        detailPanel.getComponents()[componentIndex].text = selectedBean ?: ''
        componentIndex += 2
        detailPanel.getComponents()[componentIndex].text = selectedBean ? selectedBean['id'] : ''

        for (propName in domainTable.model.domainClassProperties) {
            componentIndex += 2
            selectedBean = selectedBean?.class?.get(selectedBean?.id)
            detailPanel.getComponents()[componentIndex].text = selectedBean ? selectedBean[propName] : ''
        }
    }

    void updateDomainList() {
        domainTable.setModel(new GenericBeanListTableModel(getSelectedDomainClass()))
        refreshDetailPanel()
    }

    def getSelectedDomainClass() {
        for (domainClass in domainClasses) {
            if (domainSelectionBox.selectedItem == domainClass.fullName) {
                return domainClass
            }
        }
    }

    void stop() { }
    void stop(Throwable reason) {}
}

class DomainSelectionChangeHandler implements IActionListener {
    private outer

    public DomainSelectionChangeHandler(newOuter) {outer = newOuter}

    public void actionPerformed(ActionEvent event) {
        outer.updateDomainList()
    }
}

class BeanSelectionChangeHandler implements IListSelectionListener {
    private outer

    public BeanSelectionChangeHandler(newOuter) {outer = newOuter}

    public void valueChanged(ListSelectionEvent event) {
        outer.updateDetailValues()
    }
}


