package ac;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class LiveTimingBean {
	public static final String FALLBACK_STR = "{\"session\": -1}"; 
	
	private String data;
	private boolean isRunning;
	
	public LiveTimingBean() {
		isRunning = false;
		data = FALLBACK_STR;
	}
	
	public void setData(String data) {
		this.data = data;
		if (data.equals(FALLBACK_STR))
			isRunning = false;
		else
			isRunning = true;
	}
	
	public String getData() {
		return data;
	}
	
	public boolean getStatus() {
		return isRunning;
	}
}
