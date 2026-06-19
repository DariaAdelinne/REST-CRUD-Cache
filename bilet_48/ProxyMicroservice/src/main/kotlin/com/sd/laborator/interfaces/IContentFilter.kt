package com.sd.laborator.interfaces

interface IContentFilter {
    fun containsForbiddenTerm(content: String): Boolean
}
