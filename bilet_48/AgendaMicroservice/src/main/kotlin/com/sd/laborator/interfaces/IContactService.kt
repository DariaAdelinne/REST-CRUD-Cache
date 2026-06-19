package com.sd.laborator.interfaces

import com.sd.laborator.model.Contact

interface IContactService {
    fun getAllContacts(): List<Contact>
    fun getContactById(id: Int): Contact?
    fun createContact(name: String, phone: String, email: String): Contact
    fun updateContact(id: Int, name: String, phone: String, email: String): Contact?
    fun deleteContact(id: Int): Boolean
}
