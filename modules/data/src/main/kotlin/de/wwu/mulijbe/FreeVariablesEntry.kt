/*
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2 of the license, or (at your option) any later version.
 */

package de.wwu.mulijbe

import org.gjt.jclasslib.structures.attributes.SubStructure
import java.io.DataInput
import java.io.DataOutput

/**
 * Contains common attributes to a local variable table entry structure.
 */
class FreeVariablesEntry : SubStructure() {

    /**
     * Index of the constant pool entry containing the name of this
     * local variable.
     */
    var nameIndex: Int = 0

    /**
     * Index of this local variable.
     */
    var index: Int = 0

    override fun readData(input: DataInput) {
        nameIndex = input.readUnsignedShort()
        index = input.readUnsignedShort()
    }

    override fun writeData(output: DataOutput) {
        output.writeShort(nameIndex)
        output.writeShort(index)
    }

    override val debugInfo: String
        get() = "with nameIndex $nameIndex, index $index"

    override val length: Int
        get() = 4

}