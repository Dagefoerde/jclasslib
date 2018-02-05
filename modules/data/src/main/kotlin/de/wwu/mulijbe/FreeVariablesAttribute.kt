/*
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2 of the license, or (at your option) any later version.
 */

/*
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2 of the license, or (at your option) any later version.
 */

package de.wwu.mulijbe

import org.gjt.jclasslib.structures.AttributeInfo
import org.gjt.jclasslib.structures.ClassFile
import org.gjt.jclasslib.structures.attributes.LocalVariableEntry
import org.gjt.jclasslib.structures.emptyArraySingleton
import java.io.DataInput
import java.io.DataOutput

/**
 * Contains common attributes to a local variable table attribute structure.
 */
class FreeVariablesAttribute(classFile: ClassFile) : AttributeInfo(classFile) {

    /**
     * Local variable associations of the parent code attribute
     */
    var localVariableEntries: Array<FreeVariablesEntry> = emptyArraySingleton()

    override fun readData(input: DataInput) {
        val freeVariablesTableLength = input.readUnsignedShort()
        localVariableEntries = Array(freeVariablesTableLength) {
            FreeVariablesEntry().apply {
                read(input)
            }
        }
    }

    override fun writeData(output: DataOutput) {
        output.writeShort(localVariableEntries.size)
        localVariableEntries.forEach { it.write(output) }
    }

    override val debugInfo: String
        get() = "with ${localVariableEntries.size} entries"

    override fun getAttributeLength(): Int = 2 + localVariableEntries.sumBy { it.length }

    companion object {
        /**
         * Name of the attribute as in the corresponding constant pool entry.
         */
        const val ATTRIBUTE_NAME = "FreeVariables"
    }

}