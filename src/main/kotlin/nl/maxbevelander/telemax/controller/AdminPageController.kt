package nl.maxbevelander.telemax.controller

import nl.maxbevelander.telemax.controller.dto.PageForm
import nl.maxbevelander.telemax.controller.dto.Paragraph
import nl.maxbevelander.telemax.service.PageService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin")
class AdminPageController(private val pageService: PageService) {

    @GetMapping
    fun list(model: Model, authentication: Authentication): String {
        model.addAttribute("pages", pageService.findAll())
        model.addAttribute("currentUser", authentication.name)
        return "admin/pages"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("pageForm", PageForm())
        model.addAttribute("isEdit", false)
        return "admin/page-form"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute pageForm: PageForm,
        authentication: Authentication,
        redirectAttributes: RedirectAttributes
    ): String {
        if (pageService.existsByPageNumber(pageForm.pageNumber)) {
            redirectAttributes.addFlashAttribute("error", "Page ${pageForm.pageNumber} already exists.")
            return "redirect:/admin/create"
        }

        if (pageForm.title.length > 20) {
            redirectAttributes.addFlashAttribute("error", "Title must be 20 characters or less.")
            return "redirect:/admin/create"
        }

        if (pageForm.pageNumber < 100 || pageForm.pageNumber > 999) {
            redirectAttributes.addFlashAttribute("error", "Page number must be between 100 and 999.")
            return "redirect:/admin/create"
        }

        val paragraphs = pageForm.paragraphs.filter { it.text.isNotBlank() }
        pageService.create(pageForm.pageNumber, pageForm.title, paragraphs, authentication.name)
        redirectAttributes.addFlashAttribute("success", "Page ${pageForm.pageNumber} created.")
        return "redirect:/admin"
    }

    @GetMapping("/{pageNumber}/edit")
    fun editForm(@PathVariable pageNumber: Int, model: Model): String {
        val page = pageService.findByPageNumber(pageNumber)
            ?: return "redirect:/admin"

        val paragraphs = pageService.parseParagraphs(page).toMutableList()
        if (paragraphs.isEmpty()) paragraphs.add(Paragraph())

        val form = PageForm(
            pageNumber = page.pageNumber,
            title = page.title,
            paragraphs = paragraphs
        )
        model.addAttribute("pageForm", form)
        model.addAttribute("isEdit", true)
        return "admin/page-form"
    }

    @PostMapping("/{pageNumber}/edit")
    fun update(
        @PathVariable pageNumber: Int,
        @ModelAttribute pageForm: PageForm,
        redirectAttributes: RedirectAttributes
    ): String {
        val page = pageService.findByPageNumber(pageNumber)
            ?: return "redirect:/admin"

        if (pageForm.title.length > 20) {
            redirectAttributes.addFlashAttribute("error", "Title must be 20 characters or less.")
            return "redirect:/admin/$pageNumber/edit"
        }

        val paragraphs = pageForm.paragraphs.filter { it.text.isNotBlank() }
        pageService.update(page, pageForm.title, paragraphs)
        redirectAttributes.addFlashAttribute("success", "Page $pageNumber updated.")
        return "redirect:/admin"
    }

    @PostMapping("/{pageNumber}/delete")
    fun delete(@PathVariable pageNumber: Int, redirectAttributes: RedirectAttributes): String {
        val page = pageService.findByPageNumber(pageNumber)
        if (page != null) {
            pageService.delete(page)
            redirectAttributes.addFlashAttribute("success", "Page $pageNumber deleted.")
        }
        return "redirect:/admin"
    }
}
