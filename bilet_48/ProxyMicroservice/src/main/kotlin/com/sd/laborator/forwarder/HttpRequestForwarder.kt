package com.sd.laborator.forwarder

import com.sd.laborator.interfaces.IRequestForwarder
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class HttpRequestForwarder(
    @Value("\${agenda.base.url}") private val agendaBaseUrl: String
) : IRequestForwarder {

    private val restTemplate = RestTemplate()

    override fun forward(method: HttpMethod, path: String, queryString: String?, body: String?): ResponseEntity<String> {
        val url = if (queryString.isNullOrBlank()) "$agendaBaseUrl$path"
                  else "$agendaBaseUrl$path?$queryString"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(body, headers)

        return try {
            restTemplate.exchange(url, method, entity, String::class.java)
        } catch (e: HttpClientErrorException) {
            ResponseEntity.status(e.statusCode).body(e.responseBodyAsString)
        } catch (e: Exception) {
            ResponseEntity.status(503).body("AgendaMicroservice unavailable: ${e.message}")
        }
    }
}
