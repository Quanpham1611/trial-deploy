package quan.dodomilktea.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;
import quan.dodomilktea.config.security.CustomAccountDetail;
import quan.dodomilktea.model.Account;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@CrossOrigin(origins = "http://localhost:3000")
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired private JwtTokenUlti jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!jwtTokenUtil.hasAuthorizationHeader(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenUtil.getAccessToken(request);
        if (!jwtTokenUtil.validateAccessToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(accessToken);

        setAuthenticationContext(accessToken, request);
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(String accessToken, HttpServletRequest request) {
        UserDetails account = getUserDetails(accessToken);
        UsernamePasswordAuthenticationToken authentication = new
                UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String accessToken) {
        Account account = new Account();
        String[] subjectArray = jwtTokenUtil.getSubject(accessToken).split(",");
        account.setAcc_id(subjectArray[0]);
        account.setEmail(subjectArray[1]);
        account.setRole(subjectArray[2]);
        CustomAccountDetail accountDetail = new CustomAccountDetail(account);
        return accountDetail;
    }
}
