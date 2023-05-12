package at.srfg.iasset.semantic.model.converter;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;

public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale locale) {
        if (locale!= null) {
            return locale.getLanguage();
        } else {
            return Locale.getDefault().getLanguage();
        }
    }

    @Override
    public Locale convertToEntityAttribute(String languageString) {
        return Locale.forLanguageTag(languageString);
    }
}
