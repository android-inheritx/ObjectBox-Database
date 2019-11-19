package com.example.objectbox.database

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Uid

@Entity
@Uid(6077996458920231825L)
data class User(
    @Id var id: Long = 0,
    var name: String? = null,
    var email: String? = null,

    @Uid(8887906490830754719L)
    var contactNumber: Long? = null
)