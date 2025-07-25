package com.gca.security;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CachingRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public CachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        body = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private final ByteArrayInputStream bytes = new ByteArrayInputStream(body);

            @Override
            public int read() {
                return bytes.read();
            }

            @Override
            public boolean isFinished() {
                return bytes.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    public String getBody() {
        if (body.length == 0) {
            return "[empty]";
        }

        try {
            return new String(body, getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            return "[unsupported encoding]";
        }
    }
}