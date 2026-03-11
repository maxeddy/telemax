package nl.maxbevelander.telemax.controller.dto

data class PageForm(
    var pageNumber: Int = 100,
    var title: String = "",
    var paragraphs: MutableList<Paragraph> = mutableListOf(Paragraph())
)
