package com.sd.laborator.controller

import com.sd.laborator.interfaces.IContactService
import com.sd.laborator.model.Contact
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class ContactRequest(val name: String, val phone: String, val email: String)

@RestController
@RequestMapping("/contacts")
class ContactController(private val contactService: IContactService) {

    @GetMapping
    fun getAllContacts(): ResponseEntity<List<Contact>> =
        ResponseEntity.ok(contactService.getAllContacts())

    @GetMapping("/{id}")
    fun getContact(@PathVariable id: Int): ResponseEntity<Contact> {
        val contact = contactService.getContactById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(contact)
    }

    @PostMapping
    fun createContact(@RequestBody request: ContactRequest): ResponseEntity<Contact> {
        val contact = contactService.createContact(request.name, request.phone, request.email)
        return ResponseEntity.status(201).body(contact)
    }

    @PutMapping("/{id}")
    fun updateContact(@PathVariable id: Int, @RequestBody request: ContactRequest): ResponseEntity<Contact> {
        val contact = contactService.updateContact(id, request.name, request.phone, request.email)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(contact)
    }

    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: Int): ResponseEntity<Void> {
        if (!contactService.deleteContact(id)) return ResponseEntity.notFound().build()
        return ResponseEntity.noContent().build()
    }
}
