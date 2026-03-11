package nl.maxbevelander.telemax.controller

import nl.maxbevelander.telemax.service.PageService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/pages")
class PageViewController(private val pageService: PageService) {

    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("pages", pageService.findAll())
        return "pages/index"
    }

    @GetMapping("/{pageNumber}")
    fun view(@PathVariable pageNumber: Int, model: Model): String {
        val page = pageService.findByPageNumber(pageNumber)
            ?: return "redirect:/pages"

        model.addAttribute("page", page)
        model.addAttribute("paragraphs", pageService.parseParagraphs(page))
        return "pages/view"
    }
}
