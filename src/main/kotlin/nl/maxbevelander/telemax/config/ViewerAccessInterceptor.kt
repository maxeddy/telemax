package nl.maxbevelander.telemax.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class ViewerAccessInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // Allow access if user is logged in as an admin
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.isAuthenticated && auth.authorities.any { it.authority == "ROLE_ADMIN" }) {
            return true
        }

        val session = request.getSession(false)
        if (session?.getAttribute("viewerAuthenticated") == true) {
            return true
        }
        response.sendRedirect("/viewer/login")
        return false
    }
}
