package com.oaat.services

import com.oaat.entities.Message
import com.oaat.repositories.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(override val repository: MessageRepository) : IService<Message>
