package com.oaat.services

import com.oaat.entities.Post
import com.oaat.repositories.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(override val repository: PostRepository) : IService<Post>
