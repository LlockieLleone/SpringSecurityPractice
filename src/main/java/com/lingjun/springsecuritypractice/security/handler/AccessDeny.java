package com.lingjun.springsecuritypractice.security.handler;

import com.lingjun.springsecuritypractice.model.common.Response;
import com.lingjun.springsecuritypractice.security.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 登录用户没有权限访问的处理
 */
@Component
public class AccessDeny implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        ServletUtils.render(request,response, Response.fail("no permission"));
    }
}
