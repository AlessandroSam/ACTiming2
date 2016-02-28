package acservlet;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Сервлет, реализующий передачу данных для лайв-тайминга. Сервлет получает данные от Python-скрипта в игре
 * в виде JSON-строки. Десериализация не производится. Эти данные ровно в том виде, в каком были получены,
 * отправляются по GET-запросу.  
 */
@WebServlet("/live")
public class LiveTiming extends HttpServlet {
	private static final String FALLBACK_STR = "{\"session\": -1}"; 
	
	private static final long serialVersionUID = 1L;
	private String data = FALLBACK_STR;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LiveTiming() {
        super();
    }
    
    private synchronized void setData(String data) {
    	this.data = data;
    }
    
    private synchronized String getData() {
    	return data;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append(getData());		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer jsonBuffer = new StringBuffer();
		String line = null;
		int lineCount = 0;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
		    	jsonBuffer.append(line);
				lineCount++;
			}
			if (lineCount > 0)
				setData(jsonBuffer.toString());
			else
				setData(FALLBACK_STR);
		} catch (Exception e) {
			System.err.println("Не удалось прочитать данные из POST-запроса.");
			setData(FALLBACK_STR);
		}
	}

}
