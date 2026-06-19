package com.sd.laborator.interfaces

interface ICredentialValidator {
    fun validate(username: String, password: String): Boolean
}
