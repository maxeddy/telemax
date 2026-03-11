package nl.maxbevelander.telemax.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "pages")
class Page(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "page_number", nullable = false, unique = true)
    var pageNumber: Int = 100,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var content: String = "[]",

    @Column(name = "created_by")
    var createdBy: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: String = "",

    @Column(name = "updated_at", nullable = false)
    var updatedAt: String = ""
)
