package at.srfg.iasset.semantic.model.skos;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import at.srfg.iasset.semantic.model.skos.SKOSLabel.LabelType;

public interface LabelAware<T extends SKOSLabel> {
	/**
	 * Getter for the list of all {@link SKOSLabel} elements
	 * @return The collection, must not return <code>null</code>
	 */
	public Collection<T> getLabels();
	/**
	 * Setter the list of {@link SKOSLabel} objects.  
	 *  
	 * @param labels
	 */
	public void setLabels(Collection<T> labels);
	/**
	 * Create a new {@link SKOSLabel} with the requested locale, type and label
	 * @param locale
	 * @param labelType
	 * @param label
	 * @return
	 */
	public T newLabel(Locale locale, LabelType labelType, String label);
	
	/**
	 * Retrieve the (first) {@link ConceptLabel} for the requested language and type, assuming
	 * 
	 * @param locale
	 * @param forType
	 * @return
	 */
	default Optional<T> getLabel(Locale locale, LabelType forType) {
		if ( getLabels().isEmpty()) {
			return Optional.empty();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(forType) && 
							   t.getLocale().equals(locale);
					}
				})
				.findFirst();
	}
	default Set<Locale> getLanguages() {
		if ( getLabels().isEmpty()) {
			return Collections.emptySet();
		}
		return getLabels().stream()
				.map(c -> c.getLocale())
				.collect(Collectors.toSet());
	}
	default void setLanguages(Set<Locale> locale) {
		// nothing to do - is provided elsewhere
	}
	/**
	 * Retrieve the set of strings stored for the requested language and {@link LabelType}
	 * @param locale The language
	 * @param forType 
	 * @return
	 */
	default Set<String> getLabels(Locale locale, LabelType forType) {
		if ( getLabels().isEmpty()) {
			return Collections.emptySet();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(forType) && 
							   t.getLocale().equals(locale);
					}
				})
				.map(new Function<T, String>() {

					@Override
					public String apply(T t) {
						// TODO Auto-generated method stub
						return t.getLabel();
					}
				})
				.collect(Collectors.toSet());
	}
	/**
	 * 
	 * @param locale
	 * @return
	 */
	default Optional<String> getPreferredLabel(Locale locale) {
		Optional<T> c = getLabel(locale, LabelType.prefLabel);
		if ( c.isPresent()) {
			return Optional.of(c.get().getLabel());
		}
		return Optional.empty();
	}
	default Optional<String> getPreferredLabel(String language) {
		return getPreferredLabel(Locale.forLanguageTag(language));
	}
	/**
	 * Setter for the preferred label
	 * @param locale
	 * @param label
	 * @return
	 */
	default void setPreferredLabel(Locale locale, @NotNull String label) {
		
		Optional<T> desc = getLabel(locale, LabelType.prefLabel);
		if ( desc.isPresent()) {
			if ( label == null || label.length() == 0) {
//			if ( Strings.isNullOrEmpty(label)) {
				getLabels().remove(desc.get());
			}
			else {
				// we have a prefLabel - update it
				desc.get().setLabel(label);
			}
		}
		else {
			if ( label == null || label.length() == 0) {
//			if (! Strings.isNullOrEmpty(label)) {
				Optional<T> other = checkLabel(locale,  label);
				if ( other.isPresent()) {
					T cd = other.get();
					// promote the other label to a prefLabel
					cd.setLabelType(LabelType.prefLabel);
					cd.setLabel(label);
				}
				else {
					// create new label
					T newLabel = newLabel(locale, LabelType.prefLabel, label);
					getLabels().add(newLabel);
				}
			}
		}		
	}
	default void setPreferredLabel(@NotNull Map<Locale, String> map) {
		for (Locale locale : map.keySet()) {
			setPreferredLabel(locale, map.get(locale));
		}
	}
	default void setAlternateLabel(Map<Locale, Set<String>> map) {
		for (Locale locale : map.keySet()) {
			setAlternateLabel(locale, map.get(locale));
		}
	}
	default void setAlternateLabel(Locale locale, Set<String> alternate) {
		// 
		Collection<String> existing = getAlternateLabel(locale);
		for (String alt : alternate) {
			if ( existing.contains(alt)) {
				// already stored - no action
				existing.remove(alt);
			}
			else {
				// not yet stored - add it
				addAlternateLabel(locale, alt);
			}
		}
		// is there an existing label not yet processed?
		// they are no longer in the set of alternate labels, so remove them
		for ( String remaining : existing ) {
			Optional<T> toRemove = checkLabel(locale, remaining);
			if ( toRemove.isPresent()) {
				getLabels().remove(toRemove.get());
			}
		}
	}
	default void addAlternateLabel(Locale locale, @NotNull String alternate) {
		Optional<T> alt = checkLabel(locale, alternate);
		if ( alt.isPresent() ) {
			
			T cd = alt.get();
			switch (cd.getLabelType()) {
			case prefLabel:
			case altLabel:
			case hiddenLabel:
				cd.setLabelType(LabelType.altLabel);
				cd.setLabel(alternate);
				break;
			default:
				// either definition or comment - they may coexist with pref-, alt- and hiddenLabel
				T newLabel = newLabel(locale, LabelType.altLabel, alternate);
				getLabels().add(newLabel);
			}
		}
		else {
			// create new label
			T newLabel = newLabel(locale, LabelType.altLabel, alternate);
			getLabels().add(newLabel);

		}

	}
	default void setHiddenLabel(Map<Locale, Set<String>> map) {
		for (Locale locale : map.keySet()) {
			setHiddenLabel(locale, map.get(locale));
		}
	}

	default void setHiddenLabel(Locale locale, Set<String> alternate) {
		// 
		Collection<String> existing = getHiddenLabel(locale);
		for (String alt : alternate) {
			if ( existing.contains(alt)) {
				// already stored - no action
				existing.remove(alt);
			}
			else {
				// not yet stored - add it
				addHiddenLabel(locale, alt);
			}
		}
		// is there an existing label not yet processed?
		// they are no longer in the set of alternate labels, so remove them
		for ( String remaining : existing ) {
			Optional<T> toRemove = checkLabel(locale, remaining);
			if ( toRemove.isPresent()) {
				getLabels().remove(toRemove.get());
			}
		}
		
	}
	default void addHiddenLabel(Locale locale, @NotNull String alternate) {
		Optional<T> alt = checkLabel(locale, alternate);
		if ( alt.isPresent() ) {
			
			T cd = alt.get();
			switch (cd.getLabelType()) {
			case prefLabel:
			case altLabel:
			case hiddenLabel:
				cd.setLabelType(LabelType.hiddenLabel);
				cd.setLabel(alternate);
				break;
			default:
				// either definition or comment - they may coexist
				T newLabel = newLabel(locale, LabelType.hiddenLabel, alternate);
				getLabels().add(newLabel);
			}
		}
		else {
			// create new label
			T newLabel = newLabel(locale, LabelType.hiddenLabel, alternate);
			getLabels().add(newLabel);

		}

	}
	default void setDefinition(@NotNull Map<Locale, String> map) {
		if ( map != null) {
			for (Locale locale : map.keySet()) {
				setDefinition(locale, map.get(locale));
			}
		}
	}
	/**
	 * Setter for the definition
	 * @param locale
	 * @param definition
	 * @return
	 */
	default void setDefinition(Locale locale, @NotNull String definition) {
		Optional<T> desc = getLabel(locale, LabelType.definition);
		if ( desc.isPresent()) {
			if ( definition == null || definition.length() == 0) {
//			if ( Strings.isNullOrEmpty(definition)) {
				getLabels().remove(desc.get());
			}
			else {
				// we have a prefLabel - update it
				desc.get().setLabel(definition);
			}
		}
		else {
			if ( definition == null || definition.length() == 0) {
//			if (! Strings.isNullOrEmpty(definition)) {
				Optional<T> other = checkLabel(locale,  definition);
				if ( other.isPresent()) {
					T cd = other.get();
					// promote the other label to a prefLabel
					cd.setLabelType(LabelType.definition);
					cd.setLabel(definition);
				}
				else {
					// create new label
					T newLabel = newLabel(locale, LabelType.definition, definition);
					getLabels().add(newLabel);
				}
			}
		}		
	}
	default void setComment(@NotNull Map<Locale, String> map) {
		for (Locale locale : map.keySet()) {
			setComment(locale, map.get(locale));
		}
	}
	/**
	 * Setter for the comment
	 * @param locale
	 * @param comment
	 * @return
	 */
	default void setComment(Locale locale, @NotNull String comment) {
		Optional<T> desc = getLabel(locale, LabelType.comment);
		if ( desc.isPresent()) {
			if ( comment == null || comment.length() == 0) {
//			if ( Strings.isNullOrEmpty(comment)) {
				getLabels().remove(desc.get());
			}
			else {
			// we have a prefLabel - update it
				desc.get().setLabel(comment);
			}
		}
		else {
			if ( comment == null || comment.length() == 0) {
//			if (! Strings.isNullOrEmpty(comment)) {
				Optional<T> other = checkLabel(locale,  comment);
				if ( other.isPresent()) {
					T cd = other.get();
					// promote the other label to a prefLabel
					cd.setLabelType(LabelType.comment);
					cd.setLabel(comment);
				}
				else {
					// create new label
					T newLabel = newLabel(locale, LabelType.comment, comment);
					getLabels().add(newLabel);
				}
			}
		}		
	}
	/**
	 * Retrieve the set of alternate labels
	 * @param locale
	 * @return
	 */
	default Set<String> getAlternateLabel(Locale locale) {
		return getLabels(locale, LabelType.altLabel);
	}
	default Set<String> getAlternateLabel(String lang) {
		return getAlternateLabel(Locale.forLanguageTag(lang));
	}
	default Map<Locale, String> getPreferredLabel() {
		if ( getLabels().isEmpty()) {
			return Collections.emptyMap();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(LabelType.prefLabel);
					}
				})
				// create a map
				.collect(Collectors.toMap(T::getLocale, c -> c.getLabel()));
	}
	default Map<Locale, Set<String>> getAlternateLabel() {
		if ( getLabels().isEmpty()) {
			return Collections.emptyMap();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(LabelType.altLabel);
					}
				})
				// create a map
				.collect(
						Collectors.groupingBy(
								// the group
								T::getLocale,
								// a mapping function
								Collectors.mapping(T::getLabel, Collectors.toSet())));
	}
	default Map<Locale, Set<String>> getHiddenLabel() {
		if ( getLabels().isEmpty()) {
			return Collections.emptyMap();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(LabelType.hiddenLabel);
					}
				})
				// create a map
				.collect(
						Collectors.groupingBy(
								// the group
								T::getLocale,
								// a mapping function
								Collectors.mapping(T::getLabel, Collectors.toSet())));
	}
	default Map<Locale, String> getDefinition() {
		if ( getLabels().isEmpty()) {
			return Collections.emptyMap();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(LabelType.definition);
					}
				})
				// create a map
				.collect(Collectors.toMap(T::getLocale, c -> c.getLabel()));
	}
	default Map<Locale, String> getComment() {
		if ( getLabels().isEmpty()) {
			return Collections.emptyMap();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper type && language 
						return t.getLabelType().equals(LabelType.comment);
					}
				})
				// create a map
				.collect(Collectors.toMap(T::getLocale, c -> c.getLabel()));
	}

	default Set<String> getHiddenLabel(Locale locale) {
		return getLabels(locale, LabelType.hiddenLabel);
	}
	default Set<String> getHiddenLabel(String lang) {
		return getHiddenLabel(Locale.forLanguageTag(lang));
	}
	/**
	 * Check whether the label exists
	 * @param locale The locale of the label
	 * @param label The (expected) label
	 * @return 
	 */
	default Optional<T> checkLabel(Locale locale, String label) {
		if ( getLabels().isEmpty()) {
			return Optional.empty();
		}
		return getLabels().stream()
				.filter(new Predicate<T>() {
					@Override
					public boolean test(T t) {
						// check for proper language and the text ... 
						return t.getLocale().equals(locale) &&
							   t.getLabel().equalsIgnoreCase(label);
					}
				})
				.findFirst();
	}
	
}
