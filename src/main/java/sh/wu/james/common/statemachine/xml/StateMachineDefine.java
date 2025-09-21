package sh.wu.james.common.statemachine.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "StateMachine")
public class StateMachineDefine {

	@XmlAttribute(name = "name", required = true)
	private String name;

	@XmlElement(name = "BizInterface", required = true)
	private BizInterface bizInterface;

	@XmlElement(name = "Payload", required = true)
	private Payload payload;

	@XmlElement(name = "StatusStateMapping", required = true)
	private StatusStateMappings mapping;

	@XmlElementWrapper(name = "Listeners")
	@XmlElement(name = "Listener")
	private List<ListenerDefine> listenersDefine;

	@XmlElementWrapper(name = "States")
	@XmlElement(name = "State")
	private List<State> states;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BizInterface getBizInterface() {
		return bizInterface;
	}

	public void setBizInterface(BizInterface bizInterface) {
		this.bizInterface = bizInterface;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public StatusStateMappings getMapping() {
		return mapping;
	}

	public void setMapping(StatusStateMappings mapping) {
		this.mapping = mapping;
	}

	public List<ListenerDefine> getListenersDefine() {
		return listenersDefine;
	}

	public void setListenersDefine(List<ListenerDefine> listenersDefine) {
		this.listenersDefine = listenersDefine;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

}
