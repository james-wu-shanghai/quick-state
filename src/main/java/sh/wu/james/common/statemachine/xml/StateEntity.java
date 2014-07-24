package sh.wu.james.common.statemachine.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class StateEntity {
	@XmlAttribute(name = "fieldName", required = true)
	private String fieldName;

	@XmlAttribute(name = "payloadFieldName", required = true)
	private String payloadFieldName;

	public String getPayloadFieldName() {
		return payloadFieldName;
	}

	public void setPayloadFieldName(String payloadFieldName) {
		this.payloadFieldName = payloadFieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
