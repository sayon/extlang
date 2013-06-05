package com.extlang.engine

import com.intellij.lang.Language

public open class ELLanguage(): Language("EL") {
    class object {
        public val INSTANCE: ELLanguage? = ELLanguage()
    }
}
