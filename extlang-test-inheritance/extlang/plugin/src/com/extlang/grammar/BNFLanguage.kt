package com.extlang.grammar

import com.intellij.lang.Language

public class BNFLanguage(): Language("BNF") {
    class object {
        public val INSTANCE: BNFLanguage? = BNFLanguage()
    }
}
