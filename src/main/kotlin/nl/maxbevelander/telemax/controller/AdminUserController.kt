package nl.maxbevelander.telemax.controller

import nl.maxbevelander.telemax.controller.dto.UserForm
import nl.maxbevelander.telemax.service.UserService
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
@RequestMapping("/admin/users")
class AdminUserController(private val userService: UserService) {

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("users", userService.findAll())
        return "admin/users"
    }

    @GetMapping("/create")
    fun createForm(model: Model): String {
        model.addAttribute("userForm", UserForm())
        return "admin/user-form"
    }

    @PostMapping("/create")
    fun create(
        @ModelAttribute userForm: UserForm,
        redirectAttributes: RedirectAttributes,
        authentication: Authentication,
    ): String {
        if (userForm.username.isBlank() || userForm.username.length > 50) {
            redirectAttributes.addFlashAttribute("error", "Username is required and must be 50 characters or less.")
            return "redirect:/admin/users/create"
        }

        if (userForm.password.length < 8) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters.")
            return "redirect:/admin/users/create"
        }

        if (userForm.role !in listOf("USER", "ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Role must be USER or ADMIN.")
            return "redirect:/admin/users/create"
        }

        if (userService.existsByUsername(userForm.username)) {
            redirectAttributes.addFlashAttribute("error", "Username '${userForm.username}' already exists.")
            return "redirect:/admin/users/create"
        }

        if (!authentication.name.equals("max", true)) {
            redirectAttributes.addFlashAttribute("error", "Only 'max' can create users :)")
            return "redirect:/admin/users/create"
        }

        userService.create(userForm.username, userForm.password, userForm.role)
        redirectAttributes.addFlashAttribute("success", "User '${userForm.username}' created.")
        return "redirect:/admin/users"
    }

    @PostMapping("/{id}/delete")
    fun delete(
        @PathVariable id: Long,
        authentication: Authentication,
        redirectAttributes: RedirectAttributes
    ): String {
        val user = userService.findById(id)
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.")
            return "redirect:/admin/users"
        }

        if (user.username == authentication.name) {
            redirectAttributes.addFlashAttribute("error", "You cannot delete your own account.")
            return "redirect:/admin/users"
        }

        if (user.role == "ADMIN" && userService.countByRole("ADMIN") <= 1) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete the last admin user.")
            return "redirect:/admin/users"
        }

        if (!authentication.name.equals("max", true)) {
            redirectAttributes.addFlashAttribute("error", "Only 'max' can delete users :)")
            return "redirect:/admin/users/create"
        }

        userService.delete(user)
        redirectAttributes.addFlashAttribute("success", "User '${user.username}' deleted.")
        return "redirect:/admin/users"
    }
}
