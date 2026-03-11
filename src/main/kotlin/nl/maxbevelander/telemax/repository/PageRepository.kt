package nl.maxbevelander.telemax.repository

import nl.maxbevelander.telemax.entity.Page
import org.springframework.data.jpa.repository.JpaRepository

interface PageRepository : JpaRepository<Page, Long> {
    fun findByPageNumber(pageNumber: Int): Page?
    fun findAllByOrderByPageNumberAsc(): List<Page>
    fun existsByPageNumber(pageNumber: Int): Boolean
    fun findFirstByPageNumberLessThanOrderByPageNumberDesc(pageNumber: Int): Page?
    fun findFirstByPageNumberGreaterThanOrderByPageNumberAsc(pageNumber: Int): Page?
}
