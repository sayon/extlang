package com.extlang.engine.parser.machine

import java.util.ArrayList
import com.extlang.engine.QTree
import com.extlang.engine.QTreeNode
import com.extlang.engine.QTreeTermNode
import com.extlang.engine.QTreeNonTermNode
import com.extlang.engine.QTreeRefNode

/**A program is a list of instructions*/

class Program: ArrayList<Instruction>()
{

    public override fun toString(): String =
            fold("", {(acc, elem) -> acc + "\n" + elem.toString() })

    class object {
        // Constructs a program to build a tree similar to QTree provided.
        public fun fromQTree(tree: QTree): Program
        {
            //dfs starting with t while adding elements to prog
            fun traverse (t: QTreeNode, prog: Program)
            {
                when (t)
                {
                    is QTreeTermNode ->
                        prog.add(Instruction.TerminalNode(t.Term))
                    is QTreeNonTermNode -> {
                        prog.add(Instruction.NonTerminalNodeOpen(t.NonTerm))
                        for( c in t.Children )
                            traverse(c, prog)
                        prog.add(Instruction.NonTerminalNodeClose())
                    }
                    is QTreeRefNode ->
                        prog.add(Instruction.InsertTree(t.RefName, true))
                    else -> {
                    }
                }
            }
            val ret = Program()
            traverse(tree.Root!!, ret)
            return ret
        }
    }
}

