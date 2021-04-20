package com.mez.api.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, PUT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "60");
        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        chain.doFilter(request, resp);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

}
