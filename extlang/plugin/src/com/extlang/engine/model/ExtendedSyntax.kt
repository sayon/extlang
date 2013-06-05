package com.extlang.engine.model

import com.extlang.engine.FixedSyntax
import com.extlang.engine.TreeTransformations


public class  ExtendedSyntax: FixedSyntax()
{
    public var Transformations: TreeTransformations? = null
    class object {
        public var Instance: ExtendedSyntax = ExtendedSyntax()
    }
}
