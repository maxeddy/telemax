package nl.maxbevelander.telemax.controller

import nl.maxbevelander.telemax.entity.Page
import nl.maxbevelander.telemax.service.PageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PageViewController(private val pageService: PageService) {

    @GetMapping("/")
    fun index(model: Model): String {
        val page = pageService.findByPageNumber(100)
            ?: run {
                model.addAttribute("pages", pageService.findAll())
                return "pages/index"
            }
        populatePageModel(model, page)
        return "pages/view"
    }

    @GetMapping("/{pageNumber:\\d+}")
    fun view(@PathVariable pageNumber: Int, model: Model): String {
        val page = pageService.findByPageNumber(pageNumber)
            ?: return "redirect:/"

        populatePageModel(model, page)
        return "pages/view"
    }

    private fun populatePageModel(model: Model, page: Page) {
        model.addAttribute("page", page)
        model.addAttribute("paragraphs", pageService.parseParagraphs(page))
        model.addAttribute("prevPage", pageService.findPreviousPageNumber(page.pageNumber))
        model.addAttribute("nextPage", pageService.findNextPageNumber(page.pageNumber))
    }
}
