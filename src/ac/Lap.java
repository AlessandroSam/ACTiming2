package ac;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 

@Named
@RequestScoped
public class Lap implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private int lapNumber;           // номер круга в сессии, начиная с 1
	private int lapTime;             // время круга - целое число миллисекунд
	private boolean isValid = true;  // корректен ли круг (пока не работает)
	private long sessionId;          // идентификатор сессии в БД
	
	/* Номер первого круга сессии может быть и больше 1 в случае, если запись в 
	*  БД началась после того, как игрок проехал несколько кругов в текущей сессии.
	*/
	
	public Lap() {
		lapNumber = 0;
		lapTime = 0;
		sessionId = 0;
	}
	
	public Lap(int lapNumber, int lapTime, boolean isValid, long sessionId) {
		this.lapNumber = lapNumber;
		this.lapTime = lapTime;
		this.isValid = isValid;
		this.sessionId = sessionId;
	}
	
	public int getLapNumber() {
		return lapNumber;
	}
	
	public int getLapTime() {
		return lapTime;
	}
	
	public boolean isLapValid() {
		return isValid;
	}
	
	public long getSessionId() {
		return sessionId;
	}
	
	public void setLapNumber(int lapNumber) {
		this.lapNumber = lapNumber;
	}
	
	public void setLapTime(int lapTime) {
		this.lapTime = lapTime;
	}
	
	public void setValidFlag(boolean isValid) {
		this.isValid = isValid;
	}
	
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
}
