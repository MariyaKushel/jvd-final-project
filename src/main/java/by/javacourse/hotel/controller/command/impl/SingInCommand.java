package by.javacourse.hotel.controller.command.impl;

import by.javacourse.hotel.controller.command.*;
import by.javacourse.hotel.controller.command.Command;
import by.javacourse.hotel.controller.command.CommandResult;
import static by.javacourse.hotel.controller.command.CommandResult.SendingType.*;
import static by.javacourse.hotel.controller.command.SessionAttribute.*;
import by.javacourse.hotel.controller.command.PagePath;
import by.javacourse.hotel.entity.User;
import by.javacourse.hotel.exception.ServiceException;
import by.javacourse.hotel.model.service.ServiceProvider;
import by.javacourse.hotel.model.service.UserService;
import by.javacourse.hotel.util.CurrentPageExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Optional;

public class SingInCommand implements Command {

    static Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = request.getParameter(RequestParameter.EMAIL);
        String password = request.getParameter(RequestParameter.PASSWORD);

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();

        CommandResult commandResult = null;
        try {
            Optional<User> optUser = service.authenticate(email, password);
            if (optUser.isPresent()) {
                User user = optUser.get();
                String role = user.getRole().toString();
                String userId = String.valueOf(user.getEntityId());
                BigDecimal balance = user.getBalance();
                session.removeAttribute(WRONG_EMAIL_OR_PASSWORD);
                session.setAttribute(CURRENT_USER_ID, userId);
                session.setAttribute(CURRENT_USER, email);
                session.setAttribute(CURRENT_ROLE, role);
                session.setAttribute(CURRENT_PAGE, PagePath.SING_IN_PAGE);
                commandResult = new CommandResult(PagePath.HOME_PAGE, REDIRECT);
            } else {
                session.setAttribute(WRONG_EMAIL_OR_PASSWORD, true);
                session.setAttribute(CURRENT_PAGE, PagePath.SING_IN_PAGE);
                commandResult = new CommandResult(PagePath.SING_IN_PAGE, REDIRECT);
            }
        } catch (ServiceException e) {
            logger.error("Try to execute SingInCommand was failed " + e);
            commandResult = new CommandResult(PagePath.ERROR_500_PAGE, ERROR, 500);
        }

        return commandResult;
    }
}
