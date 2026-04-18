package com.saadoun.yahoo.config;

import com.saadoun.yahoo.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.net.ssl.HandshakeCompletedListener;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            Authentication auth =
                    (Authentication) httpRequest.getUserPrincipal();

            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {

                attributes.put("userId", user.getId());
                attributes.put("username", user.getUsername());
            }
        }

        return true;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
