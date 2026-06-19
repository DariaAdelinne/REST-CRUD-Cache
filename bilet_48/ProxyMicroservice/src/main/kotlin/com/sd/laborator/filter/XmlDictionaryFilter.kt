package com.sd.laborator.filter

import com.sd.laborator.interfaces.IContentFilter
import org.springframework.stereotype.Component
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

@Component
class XmlDictionaryFilter : IContentFilter {

    private val forbiddenTerms: List<String> by lazy { loadDictionary() }

    private fun loadDictionary(): List<String> {
        val stream = javaClass.classLoader.getResourceAsStream("dictionary.xml")
            ?: error("dictionary.xml not found")
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
        val terms = doc.getElementsByTagName("term")
        return (0 until terms.length).map { terms.item(it).textContent.trim().lowercase() }
    }

    override fun containsForbiddenTerm(content: String): Boolean {
        val lowerContent = content.lowercase()
        return forbiddenTerms.any { term -> lowerContent.contains(term) }
    }
}
