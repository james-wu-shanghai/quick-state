package sh.wu.james.common.statemachine.xml;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlFactory {

	@SuppressWarnings("unchecked")
	public static <T> T unmarshall(InputStream xml, Class<T> clazz) {
		T obj = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz.getPackage().getName());
			Unmarshaller u = jc.createUnmarshaller();
			obj = (T) u.unmarshal(xml);
		} catch (JAXBException e) {
			throw new RuntimeException("Can't unmarshal the XML file, error message: " + e.getMessage(), e);
		}
		return obj;
	}

	public static <T> String marshal(T obj, Class<T> clazz) {
		String result = null;

		try {
			JAXBContext jc = JAXBContext.newInstance(clazz.getPackage().getName());
			Marshaller m = jc.createMarshaller();
			StringWriter writer = new StringWriter();
			m.marshal(obj, writer);
			result = writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException("Can't marshal the XML file, error message: " + e.getMessage());
		}

		return result;
	}
}
