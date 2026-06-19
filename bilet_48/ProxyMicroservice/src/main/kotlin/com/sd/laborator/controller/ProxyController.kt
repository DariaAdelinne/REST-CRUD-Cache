package com.sd.laborator.controller

import com.sd.laborator.interfaces.IContentFilter
import com.sd.laborator.interfaces.ICredentialValidator
import com.sd.laborator.interfaces.IRequestForwarder
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Base64

/*
 * ProxyController — intercepteaza toate cererile, le valideaza si le filtreaza
 * inainte de a le trimite mai departe catre AgendaMicroservice.
 *
 * Autentificare: Basic Auth (header Authorization: Basic base64(username:password))
 * Filtrare: verifica URL + body pentru termeni interzisi din dictionary.xml
 *
 * Coregrafie: ProxyMicroservice apeleaza direct AgendaMicroservice (service chaining),
 * nu exista orchestrator central.
 */
@RestController
class ProxyController(
    private val credentialValidator: ICredentialValidator,
    private val contentFilter: IContentFilter,
    private val requestForwarder: IRequestForwarder
) {

    @RequestMapping("/**")
    fun proxy(
        request: HttpServletRequest,
        @RequestBody(required = false) body: String?
    ): ResponseEntity<String> {

        // Pasul 1: autentificare din header Authorization: Basic base64(user:pass)
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401).body("Unauthorized: lipseste header-ul Authorization")
        }

        val decoded = String(Base64.getDecoder().decode(authHeader.removePrefix("Basic ").trim()))
        val parts = decoded.split(":", limit = 2)
        if (parts.size != 2 || !credentialValidator.validate(parts[0], parts[1])) {
            return ResponseEntity.status(401).body("Unauthorized: credentiale invalide")
        }

        // Pasul 2: filtrare continut (URL + body) dupa dictionarul XML
        val contentToCheck = "${request.requestURI} ${request.queryString ?: ""} ${body ?: ""}"
        if (contentFilter.containsForbiddenTerm(contentToCheck)) {
            return ResponseEntity.status(404).body("Not Found: cererea contine termeni interzisi")
        }

        // Pasul 3: forwarding catre AgendaMicroservice
        val method = HttpMethod.valueOf(request.method)
        return requestForwarder.forward(method, request.requestURI, request.queryString, body)
    }
}
