package booking.filters;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal implements UserDetails {
    private final Long Id;
    private final String username;
    private final List<GrantedAuthority> authorities;

    public CustomUserPrincipal(Long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        this.Id = userId;
        this.username = username;
        this.authorities = new ArrayList<>(authorities);
    }

    // Expose userId
    public Long getUserId() {
        return Id;
    }

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // not used in JWT auth
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
