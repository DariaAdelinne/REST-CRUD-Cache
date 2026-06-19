package com.sd.laborator.interfaces

import com.sd.laborator.model.Contact

interface IContactRepository {
    fun findAll(): List<Contact>
    fun findById(id: Int): Contact?
    fun save(contact: Contact): Contact
    fun update(id: Int, contact: Contact): Contact?
    fun delete(id: Int): Boolean
}
