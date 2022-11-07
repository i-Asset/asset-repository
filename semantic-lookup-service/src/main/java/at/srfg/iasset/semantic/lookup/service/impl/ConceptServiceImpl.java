package at.srfg.iasset.semantic.lookup.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.srfg.iasset.semantic.lookup.repository.ConceptRepository;
import at.srfg.iasset.semantic.lookup.service.ConceptService;
import at.srfg.iasset.semantic.model.ConceptBase;
/**
 * Service for dealing with semantic lookup concepts (ConceptClass, Property, PropertyUnit, PropertyValue)
 * @author dglachs
 *
 * @param <T>
 */
@Service
public abstract class ConceptServiceImpl<T extends ConceptBase> implements ConceptService<T> {
	private Logger logger = LoggerFactory.getLogger(ConceptServiceImpl.class);
	@Autowired
	private ConceptRepository<ConceptBase> baseRepository;
	@Autowired
	protected ConceptRepository<T> typeRepository;
	public <O extends ConceptBase> Optional<O> getConcept(String identifier, Class<O> clazz) {
		if (! isNullOrEmpty(identifier)) {
			Optional<ConceptBase> concept = baseRepository.findByConceptId(identifier);
			if ( concept.isPresent() ) {
				ConceptBase base = concept.get();
				if ( clazz.isInstance(base)) {
					return Optional.of(clazz.cast(base));
				}
			}
		}
		return Optional.empty();
	}
	@Transactional
	@Override
	public long deleteNameSpace(String nameSpace) {
		return typeRepository.deleteByNameSpace(nameSpace);
	}
	/**
	 * Read the stored concept based on it's {@link ConceptBase#getConceptId()} prior
	 * to updating.
	 * @param updated The concept for updating, must provide the {@link ConceptBase#getConceptId()}
	 * @return
	 */
	protected Optional<T> getStoredConcept(T updated) {
		if (! isNullOrEmpty(updated.getConceptId())) {
			logger.info("Searching for {}", updated.getConceptId());
			return typeRepository.findByConceptId(updated.getConceptId());
		}
		return Optional.empty();
	}
	public Optional<T> getConcept(String identifier) {
		return typeRepository.findByConceptId(identifier);
	}
	public Optional<T> getConcept(String nameSpace, String localName) {
		return typeRepository.findByNameSpaceAndLocalName(nameSpace, localName);
	}
	public boolean deleteConcept(String identifier) {
		Optional<T> base = typeRepository.findByConceptId(identifier);
		if (base.isPresent()) {
			typeRepository.delete(base.get());
			return true;
		}
		return false;
	}
//	@Override
//	public Optional<T> setDescription(String identifier, ConceptBaseDescription desc) {
//		Optional<T> baseOpt = getConcept(identifier);
//		if ( baseOpt.isPresent()) {
//			T base = baseOpt.get();
//			base.setDescription(desc.getLanguage(), desc.getPreferredName(), desc.getDefinition());
//			return Optional.of(base);
//		}
//		return baseOpt;
//	}
	@Override
	public boolean conceptExists(String identifier) {
		return baseRepository.existsByConceptId(identifier);
	}
	protected boolean isNullOrEmpty(String string) {
		if ( string == null) return false;
		if ( string.length() == 0 ) return false;
		return true;
	}
	protected String padEnd(String string, int length, String character) {
		StringBuffer buf = new StringBuffer(string);
		for ( int i = string.length(); i<length; i++ ) {
			buf.append(character);
		}
		return buf.toString();
	}
}
