package sh.wu.james.common.statemachine.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusStateMappings {
	@XmlElement(name = "Empty")
	private EmptyStatusStateMap empty;

	@XmlElement(name = "Map", required=true)
	private List<StatusStateMapping> map;

	public EmptyStatusStateMap getEmpty() {
		return empty;
	}

	public void setEmpty(EmptyStatusStateMap empty) {
		this.empty = empty;
	}

	public List<StatusStateMapping> getMap() {
		return map;
	}

	public void setMap(List<StatusStateMapping> map) {
		this.map = map;
	}

}
