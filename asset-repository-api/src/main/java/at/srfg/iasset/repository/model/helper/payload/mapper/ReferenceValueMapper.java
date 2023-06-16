package at.srfg.iasset.repository.model.helper.payload.mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.repository.model.helper.payload.PayloadValueMapper;
import at.srfg.iasset.repository.model.helper.payload.ReferenceValue;

public class ReferenceValueMapper implements PayloadValueMapper<Reference, ReferenceValue> {
	private static Pattern PATTERN = Pattern.compile("\\((\\w+)\\)([A-Za-z0-9\\[\\]:\\/.]+)[,]?+");
	
	@Override
	public ReferenceValue mapToValue(Reference modelElement) {
		return new ReferenceValue(modelElement);
	}

//
//	public static void main(String[] args) {
//		String value = "(Submodel)https://template.at,(SubmodelElementCollection)smc,(Property)prop";
//		String pattern = "\\((\\w+)\\)([A-Za-z:\\/.]+)[,]?+";
//		
//		Pattern p = Pattern.compile(pattern);
//		Matcher m = p.matcher(value);
//		while (m.find()) {
//			String g1 = m.group(1);
//			String g2 = m.group(2);
//		}
//	}
	
	private static boolean isExternalReference(String value) {
		return PATTERN.asPredicate().test(value);
	
	}

	@Override
	public Reference mapFromValue(ReferenceValue valueElement) {
		if (isExternalReference(valueElement.getValue())) {
			return new DefaultReference.Builder()
					.type(ReferenceTypes.GLOBAL_REFERENCE)
					.key(new DefaultKey.Builder()
							.type(KeyTypes.GLOBAL_REFERENCE)
							.value(valueElement.getValue())
							.build())
					.build();
		}
		else {
			DefaultReference.Builder builder = new DefaultReference.Builder();
			builder.type(ReferenceTypes.MODEL_REFERENCE);
			Matcher matcher = PATTERN.matcher(valueElement.getValue());
			while (matcher.find()) {
				builder.key(new DefaultKey.Builder()
						.type(KeyTypes.fromValue(matcher.group(1)))
						.value(matcher.group(2))
						.build());
			}
			return builder.build();
		}
	}
}
