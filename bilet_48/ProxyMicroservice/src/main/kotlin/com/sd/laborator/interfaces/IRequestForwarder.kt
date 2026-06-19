package com.sd.laborator.interfaces

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

interface IRequestForwarder {
    fun forward(method: HttpMethod, path: String, queryString: String?, body: String?): ResponseEntity<String>
}
