package by.javacourse.hotel.controller.command.impl;

import by.javacourse.hotel.controller.command.*;

import static by.javacourse.hotel.controller.command.CommandResult.SendingType.ERROR;
import static by.javacourse.hotel.controller.command.CommandResult.SendingType.FORWARD;
import static by.javacourse.hotel.controller.command.RequestAttribute.ROOM_LIST;
import static by.javacourse.hotel.controller.command.RequestParameter.*;

import by.javacourse.hotel.entity.Room;
import by.javacourse.hotel.exception.ServiceException;
import by.javacourse.hotel.model.service.RoomService;
import by.javacourse.hotel.model.service.ServiceProvider;
import by.javacourse.hotel.util.CurrentPageExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class FindRoomByParameter implements Command {
    static Logger logger = LogManager.getLogger();

    @Override
    public CommandResult execute(HttpServletRequest request) {
        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put(DATE_FROM, request.getParameter(DATE_FROM));
        searchParameters.put(DATE_TO, request.getParameter(DATE_TO));
        searchParameters.put(PRICE_FROM, request.getParameter(PRICE_FROM));
        searchParameters.put(PRICE_TO, request.getParameter(PRICE_TO));
        String[] sleepingPlaces = request.getParameterValues(SLEEPING_PLACES);

        HttpSession session = request.getSession();
        ServiceProvider provider = ServiceProvider.getInstance();
        RoomService roomService = provider.getRoomService();
        List<Room> rooms = new ArrayList<>();
        CommandResult commandResult = null;
        try {
            rooms = roomService.findRoomByParameters(searchParameters, sleepingPlaces);
            request.setAttribute(ROOM_LIST, rooms);
            if (rooms.isEmpty() && searchParameters.get(DATE_FROM).isEmpty()) {
                request.setAttribute(RequestAttribute.WRONG_DATE_OR_PRICE_RANGE, true);
            } else {
                request.setAttribute(RequestAttribute.WRONG_DATE_OR_PRICE_RANGE, false);
            }

            request.setAttribute(RequestAttribute.MIN_PRICE, request.getParameter(MIN_PRICE_FOR_SEARCH));
            request.setAttribute(RequestAttribute.MAX_PRICE, request.getParameter(MAX_PRICE_FOR_SEARCH));

            List<Integer> allSleepingPlace = roomService.findAllPossibleSleepingPlace();
            request.setAttribute(RequestAttribute.ALL_SLEEPING_PLACE_LIST, allSleepingPlace);
            request.setAttribute(RequestAttribute.TEMP_DATE_FROM, searchParameters.get(DATE_FROM));
            request.setAttribute(RequestAttribute.TEMP_DATE_TO, searchParameters.get(DATE_TO));
            request.setAttribute(RequestAttribute.TEMP_PRICE_FROM, searchParameters.get(PRICE_FROM));
            request.setAttribute(RequestAttribute.TEMP_PRICE_TO, searchParameters.get(PRICE_TO));
            session.setAttribute(SessionAttribute.CURRENT_PAGE, CurrentPageExtractor.extract(request));

            commandResult = new CommandResult(PagePath.BOOK_ROOM_PAGE, FORWARD);
        } catch (ServiceException e) {
            logger.error("Try to execute FindRoomByParameter was failed " + e);
            commandResult = new CommandResult(PagePath.ERROR_500_PAGE, ERROR, 500);
        }
        return commandResult;
    }


    private List<Integer> convertArrayToList(String[] strArray) {
        List<Integer> integerList = Stream.of(strArray).map(s -> Integer.parseInt(s)).toList();
        return integerList;
    }
}
