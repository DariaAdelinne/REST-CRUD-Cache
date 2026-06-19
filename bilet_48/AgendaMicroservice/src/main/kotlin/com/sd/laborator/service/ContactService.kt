package com.sd.laborator.service

import com.sd.laborator.interfaces.IContactRepository
import com.sd.laborator.interfaces.IContactService
import com.sd.laborator.model.Contact
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: IContactRepository) : IContactService {

    override fun getAllContacts(): List<Contact> = contactRepository.findAll()

    override fun getContactById(id: Int): Contact? = contactRepository.findById(id)

    override fun createContact(name: String, phone: String, email: String): Contact =
        contactRepository.save(Contact(0, name, phone, email))

    override fun updateContact(id: Int, name: String, phone: String, email: String): Contact? =
        contactRepository.update(id, Contact(id, name, phone, email))

    override fun deleteContact(id: Int): Boolean = contactRepository.delete(id)
}
