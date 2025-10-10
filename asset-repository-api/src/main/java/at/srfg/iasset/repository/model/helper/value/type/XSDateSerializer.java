package at.srfg.iasset.repository.model.helper.value.type;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class XSDateSerializer extends JsonSerializer<XSDateTime>{

	@Override
	public void serialize(XSDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.toString());
		
	}
}
