package sh.wu.james.common.statemachine.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Fork {
	@XmlAttribute(name = "judgeClass")
	private String judgeClass;

	@XmlElement(name = "Path")
	private List<Path> paths;

	public String getJudgeClass() {
		return judgeClass;
	}

	public void setJudgeClass(String judgeClass) {
		this.judgeClass = judgeClass;
	}

	public List<Path> getPaths() {
		return paths;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

}
