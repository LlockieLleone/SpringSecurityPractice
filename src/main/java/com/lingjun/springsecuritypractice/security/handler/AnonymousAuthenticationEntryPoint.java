package com.lingjun.springsecuritypractice.security.handler;

import com.lingjun.springsecuritypractice.model.common.Response;
import com.lingjun.springsecuritypractice.security.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * 处理匿名用户无权访问
 */
@Component
public class AnonymousAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //未登录时返回给前端数据
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        ServletUtils.render(request,response, Response.fail("need login"));
    }
}
