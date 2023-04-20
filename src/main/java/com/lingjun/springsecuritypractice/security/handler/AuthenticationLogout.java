package com.lingjun.springsecuritypractice.security.handler;

import com.lingjun.springsecuritypractice.model.common.Response;
import com.lingjun.springsecuritypractice.security.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 退出登录的回调
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ServletUtils.render(request,response, Response.success("logout success"));
    }
}
