package nl.maxbevelander.telemax.service

import nl.maxbevelander.telemax.controller.dto.Paragraph
import nl.maxbevelander.telemax.entity.Page
import nl.maxbevelander.telemax.repository.PageRepository
import org.springframework.stereotype.Service
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class PageService(
    private val pageRepository: PageRepository,
    private val objectMapper: ObjectMapper
) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun findAll(): List<Page> = pageRepository.findAllByOrderByPageNumberAsc()

    fun findByPageNumber(pageNumber: Int): Page? = pageRepository.findByPageNumber(pageNumber)

    fun existsByPageNumber(pageNumber: Int): Boolean = pageRepository.existsByPageNumber(pageNumber)

    fun create(pageNumber: Int, title: String, paragraphs: List<Paragraph>, createdBy: String?): Page {
        val now = LocalDateTime.now().format(dateTimeFormatter)
        val page = Page(
            pageNumber = pageNumber,
            title = title,
            content = objectMapper.writeValueAsString(paragraphs),
            createdBy = createdBy,
            createdAt = now,
            updatedAt = now
        )
        return pageRepository.save(page)
    }

    fun update(page: Page, title: String, paragraphs: List<Paragraph>): Page {
        page.title = title
        page.content = objectMapper.writeValueAsString(paragraphs)
        page.updatedAt = LocalDateTime.now().format(dateTimeFormatter)
        return pageRepository.save(page)
    }

    fun delete(page: Page) = pageRepository.delete(page)

    fun findPreviousPageNumber(pageNumber: Int): Int? =
        pageRepository.findFirstByPageNumberLessThanOrderByPageNumberDesc(pageNumber)?.pageNumber

    fun findNextPageNumber(pageNumber: Int): Int? =
        pageRepository.findFirstByPageNumberGreaterThanOrderByPageNumberAsc(pageNumber)?.pageNumber

    fun parseParagraphs(page: Page): List<Paragraph> {
        return objectMapper.readValue(page.content, object : TypeReference<List<Paragraph>>() {})
    }
}
