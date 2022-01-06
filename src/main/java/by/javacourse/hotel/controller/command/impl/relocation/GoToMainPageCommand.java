package by.javacourse.hotel.controller.command.impl.relocation;

import by.javacourse.hotel.controller.command.Command;
import by.javacourse.hotel.controller.command.CommandResult;
import by.javacourse.hotel.controller.command.PagePath;
import by.javacourse.hotel.controller.command.SessionAtribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import static by.javacourse.hotel.controller.command.CommandResult.SendingType.*;

public class GoToMainPageCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAtribute.CURRENT_PAGE, PagePath.MAIN_PAGE);
        return new CommandResult(PagePath.MAIN_PAGE, FORWARD);
    }
}
