package nl.maxbevelander.telemax.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/viewer")
class ViewerLoginController(
    @Value("\${telemax.viewer-password}") private val viewerPassword: String
) {

    @GetMapping("/login")
    fun loginForm(): String {
        return "viewer/login"
    }

    @PostMapping("/login")
    fun login(
        @RequestParam password: String,
        request: HttpServletRequest,
        model: Model
    ): String {
        if (password == viewerPassword) {
            request.session.setAttribute("viewerAuthenticated", true)
            return "redirect:/"
        }
        model.addAttribute("error", true)
        return "viewer/login"
    }
}
