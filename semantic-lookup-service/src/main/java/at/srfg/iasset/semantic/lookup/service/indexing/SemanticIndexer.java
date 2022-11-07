package at.srfg.iasset.semantic.lookup.service.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
/**
 * Component triggering the indexation of modified data
 * with the indexing service.
 * @author dglachs
 *
 */
@Component
public class SemanticIndexer {
	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * Asynchronously (re)index the {@link ConceptClass}
	 * @param conceptClass
	 */
	public void store(ConceptClass conceptClass) {
		publisher.publishEvent(new ConceptClassEvent(conceptClass));
	}
	/**
	 * Asynchronously (re)index the {@link ConceptProperty}
	 * @param property
	 */
	public void store(ConceptProperty property) {
		publisher.publishEvent(new PropertyEvent(property));
	}
	
	/**
	 * Asynchronously (re)index the {@link ConceptClass}
	 * @param conceptClass
	 */
	public void remove(ConceptClass conceptClass) {
		publisher.publishEvent(new ConceptClassEvent(conceptClass, true));
	}
	/**
	 * Asynchronously (re)index the {@link ConceptProperty}
	 * @param property
	 */
	public void remove(ConceptProperty property) {
		publisher.publishEvent(new PropertyEvent(property, true));
	}
	
}
