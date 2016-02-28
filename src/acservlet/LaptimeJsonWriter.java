package acservlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ac.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

/**
 * Сервлет, отправляющий данные о кругах по запросу со страницы
 */
@WebServlet("/laptimes")
public class LaptimeJsonWriter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * По AJAX-запросу с указанием id сессии отправляем список кругов в формате JSON. 
		 */
		long sessionId = Long.valueOf(request.getParameter("id"));
		SessionDAO sdao = new SessionDAO();
		List<Lap> laps = sdao.getSessionLaps(sessionId);
		response.setContentType("application/json");  // не забыть включить, иначе JSON не распарсится
		StringWriter writer = new StringWriter();  // напрямую в response.getOutputStream() почему-то пишется пустота
		JsonGenerator generator = Json.createGenerator(writer);
		generator.writeStartObject();  // Объект {
		generator.writeStartArray("laps");  // Массив laps: [
		for (Lap lap: laps) {
			generator.writeStartArray()  // Подмассив - пара значений [номер круга, время в мс] 
			.write(lap.getLapNumber())
			.write(lap.getLapTime())
			.writeEnd();  // конец подмассива ]
		}
		generator.writeEnd()  // конец массива laps [...]
		.writeEnd()
		.flush();// конец объекта }
		response.getOutputStream().print(writer.toString());
	}

}
