package com.sd.laborator.validator

import com.sd.laborator.interfaces.ICredentialValidator
import org.springframework.stereotype.Component
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

@Component
class XmlCredentialValidator : ICredentialValidator {

    private val allowedCredentials: Map<String, String> by lazy { loadCredentials() }

    private fun loadCredentials(): Map<String, String> {
        val stream = javaClass.classLoader.getResourceAsStream("credentials.xml")
            ?: error("credentials.xml not found")
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream)
        val users = doc.getElementsByTagName("user")
        val map = mutableMapOf<String, String>()
        for (i in 0 until users.length) {
            val user = users.item(i) as Element
            val username = user.getElementsByTagName("username").item(0).textContent.trim()
            val password = user.getElementsByTagName("password").item(0).textContent.trim()
            map[username] = password
        }
        return map
    }

    override fun validate(username: String, password: String): Boolean =
        allowedCredentials[username] == password
}
