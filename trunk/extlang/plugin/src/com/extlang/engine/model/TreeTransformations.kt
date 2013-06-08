package com.extlang.engine.model

import com.extlang.engine.model.Rule
import java.util.HashMap
import com.extlang.engine.QTree


/** Defines how trees should be built based on certain rules.
*/
public class TreeTransformations
{
    // Each QTree is a description of the tree building rule parsed from a quotation tree.
    public val QTrees: HashMap<Rule, QTree> = HashMap<Rule, QTree>()

    // Maps:  Rule -> (Alias Name -> Alias position in rule)
    public val AliasesToIdx: HashMap<
            Rule,
            HashMap<String, Int>
            > = HashMap<Rule, HashMap<String, Int>>()

    public fun addAlias(rule: Rule, idx: Int, alias: String)
    {
        if (!AliasesToIdx.containsKey(rule))
            AliasesToIdx.put(rule, HashMap<String, Int>())
        AliasesToIdx[rule]!!.put(alias, idx)
        rule.setAlias(idx, alias)
    }

}
