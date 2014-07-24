package sh.wu.james.common.statemachine.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class State {

	@XmlAttribute(name = "name", required=true)
	private String name;

	@XmlElementWrapper(name = "PreProcessors")
	@XmlElement(name = "PreProcessor")
	private List<PreProcessorDefine> preProcessor;

	@XmlElementWrapper(name = "SupportMethods")
	@XmlElement(name = "SupportMethod")
	private List<SupportMethod> supportMethods;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PreProcessorDefine> getPreProcessor() {
		return preProcessor;
	}

	public void setPreProcessor(List<PreProcessorDefine> preProcessor) {
		this.preProcessor = preProcessor;
	}

	public List<SupportMethod> getSupportMethods() {
		return supportMethods;
	}

	public void setSupportMethods(List<SupportMethod> supportMethods) {
		this.supportMethods = supportMethods;
	}

}
