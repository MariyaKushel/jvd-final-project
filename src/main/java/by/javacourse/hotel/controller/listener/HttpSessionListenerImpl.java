package by.javacourse.hotel.controller.listener;

import by.javacourse.hotel.controller.command.SessionAttribute;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {
    private static final String DEFAULT_LOCALE = "ru_RU";
    private static final String DEFAULT_PAGE = "index.jsp";

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute(SessionAttribute.LOCALE,DEFAULT_LOCALE);
        session.setAttribute(SessionAttribute.CURRENT_PAGE, DEFAULT_PAGE);
    }
}
