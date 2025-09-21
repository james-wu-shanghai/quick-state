package sh.wu.james.common.statemachine.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SupportMethod {
	@XmlAttribute(name = "name", required = true)
	private String name;

	@XmlElement(name = "Fork", required = false)
	private Fork fork;

	@XmlElement(name = "NextStep", required = false)
	private NextStep nextStep;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Fork getFork() {
		return fork;
	}

	public void setFork(Fork fork) {
		this.fork = fork;
	}

	public NextStep getNextStep() {
		return nextStep;
	}

	public void setNextStep(NextStep nextStep) {
		this.nextStep = nextStep;
	}

}
