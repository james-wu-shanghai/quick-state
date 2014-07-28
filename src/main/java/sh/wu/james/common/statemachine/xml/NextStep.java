package sh.wu.james.common.statemachine.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class NextStep {

	@XmlAttribute(name = "nextStatus")
	private String nextStatus;

	@XmlElementWrapper(name = "Events")
	@XmlElement(name = "Event")
	private List<EventDefinition> events;

	public List<EventDefinition> getEvents() {
		return events;
	}

	public void setEvents(List<EventDefinition> events) {
		this.events = events;
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}

}
