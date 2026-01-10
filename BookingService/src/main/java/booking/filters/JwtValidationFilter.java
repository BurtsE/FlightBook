package booking.filters;

import booking.dto.JwtValidationResponse;
import booking.dto.UserDetailsWithRole;
import booking.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component

public class JwtValidationFilter extends OncePerRequestFilter {
    private final UserService userService;

    public JwtValidationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7); // extract token

            try {
                // Call your service to validate JWT and get user details + role
                JwtValidationResponse userDto = userService.validateAndExtractRole(jwt);

                System.out.println(userDto);
//                JwtValidationResponse userDto = new JwtValidationResponse(1L, "user1", "USER", true);

                // Convert single role to list
                List<String> roles = List.of(userDto.role());
                // Build Spring Security Authentication

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .toList();

                // Build custom principal with userId
                CustomUserPrincipal principal = new CustomUserPrincipal(
                        userDto.userId(),
                        userDto.username(),
                        authorities
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                authorities
                        );

                // Optional: add details like token, IP, etc.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Log error, but let filter chain continue → will be caught by 401 later
                SecurityContextHolder.clearContext();
                // Optionally send 401 here, but better to let access decision handle it
            }
        }

        filterChain.doFilter(request, response);
    }
}
