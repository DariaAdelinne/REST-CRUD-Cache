package com.sd.laborator.repository

import com.sd.laborator.interfaces.IContactRepository
import com.sd.laborator.model.Contact
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
class InMemoryContactRepository : IContactRepository {
    private val contacts = mutableMapOf<Int, Contact>()
    private val idCounter = AtomicInteger(1)

    override fun findAll(): List<Contact> = contacts.values.toList()

    override fun findById(id: Int): Contact? = contacts[id]

    override fun save(contact: Contact): Contact {
        val id = idCounter.getAndIncrement()
        val saved = contact.copy(id = id)
        contacts[id] = saved
        return saved
    }

    override fun update(id: Int, contact: Contact): Contact? {
        if (!contacts.containsKey(id)) return null
        val updated = contact.copy(id = id)
        contacts[id] = updated
        return updated
    }

    override fun delete(id: Int): Boolean {
        return contacts.remove(id) != null
    }
}
