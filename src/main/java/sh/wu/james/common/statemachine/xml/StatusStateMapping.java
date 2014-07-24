package sh.wu.james.common.statemachine.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatusStateMapping {
	@XmlAttribute(name = "statusMark", required = true)
	private String statusMark;

	@XmlAttribute(name = "state", required = true)
	private String stateName;

	public String getStatusMark() {
		return statusMark;
	}

	public void setStatusMark(String statusMark) {
		this.statusMark = statusMark;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

}
