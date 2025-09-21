package sh.wu.james.statemachine.xml;

import java.io.InputStream;

import org.junit.Test;

import sh.wu.james.common.statemachine.xml.StateMachineDefine;
import sh.wu.james.common.statemachine.xml.XmlFactory;

public class XmlFactoryTest {

	@Test
	public void marshall() {
		InputStream xml = getClass().getResourceAsStream("/statemachine/HelloworldStateDefine.xml");
		StateMachineDefine result = XmlFactory.unmarshall(xml, StateMachineDefine.class);
		System.out.println(result);
	}
}
