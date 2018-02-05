package de.wwu.mulijbe

import org.gjt.jclasslib.browser.BrowserServices
import org.gjt.jclasslib.browser.detail.TableDetailPane
import org.gjt.jclasslib.browser.detail.attributes.Column
import org.gjt.jclasslib.browser.detail.attributes.ColumnTableModel
import org.gjt.jclasslib.browser.detail.attributes.NamedConstantPoolLinkColumn
import org.gjt.jclasslib.browser.detail.attributes.NumberColumn
import java.util.ArrayList

class FreeVariablesAttributeDetailPane(services: BrowserServices) : TableDetailPane<FreeVariablesAttribute>(FreeVariablesAttribute::class.java, services) {

    override fun createTableModel(attribute: FreeVariablesAttribute) = AttributeTableModel(attribute.localVariableEntries)

    override val rowHeightFactor: Float
        get() = 2f

    protected inner class AttributeTableModel(rows: Array<FreeVariablesEntry>) : ColumnTableModel<FreeVariablesEntry>(rows) {

        override fun buildColumns(columns: ArrayList<Column<FreeVariablesEntry>>) {
            super.buildColumns(columns)
            columns.apply {
                add(object : NumberColumn<FreeVariablesEntry>("Index") {
                    override fun createValue(row: FreeVariablesEntry) = row.index
                })
                add(object : NamedConstantPoolLinkColumn<FreeVariablesEntry>("Name", services, 200) {
                    override fun getConstantPoolIndex(row: FreeVariablesEntry) = row.nameIndex
                })
            }
        }
    }
}
