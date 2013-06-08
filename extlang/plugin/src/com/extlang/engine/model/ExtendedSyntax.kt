package com.extlang.engine.model

import com.extlang.engine.FixedSyntax


/** This syntax can be extended. Transformations define the special tree building rules that correspond to certain
* grammar rules.
*/
public class  ExtendedSyntax: FixedSyntax()
{
    public val Transformations: TreeTransformations =  TreeTransformations()

    class object {
        public var Instance: ExtendedSyntax = ExtendedSyntax()
    }
}
