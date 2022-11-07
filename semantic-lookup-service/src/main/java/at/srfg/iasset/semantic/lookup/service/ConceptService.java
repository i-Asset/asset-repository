package at.srfg.iasset.semantic.lookup.service;

import java.util.Optional;

import at.srfg.iasset.semantic.model.ConceptBase;

public interface ConceptService<T extends ConceptBase> {
	/**
	 * Check whether a concept is present in the repository
	 * @param identifier
	 * @return
	 */
	boolean conceptExists(String identifier);
	/**
	 * Find a concept from the repository
	 * @param identifier
	 * @return
	 */
	Optional<T> getConcept(String identifier);
	/**
	 * Find a concept based on nameSpace and localName
	 * @param nameSpace
	 * @param localName
	 * @return
	 */
	Optional<T> getConcept(String nameSpace, String localName);
	/**
	 * Add a new concept
	 * @param newConcept
	 * @return
	 */
	Optional<T> addConcept(T newConcept);
	/**
	 * Provide a new version of the concept
	 * @param updatedConcept
	 * @return
	 */
	Optional<T> setConcept(T updatedConcept);
	T setConcept(T storedConcept, T updatedConcept);
	/**
	 * Delete a concept from the repository
	 * @param identifier
	 * @return
	 */
	boolean deleteConcept(String identifier);
	/**
	 * Delete all concepts of a given namespace
	 * @param nameSpace
	 * @return
	 */
	long deleteNameSpace(String nameSpace);
	/**
	 * change the description of an existing element, setting the description to null
	 * will remove an language description
	 * @param identifier
	 * @param desc
	 * @return
	 */
//	Optional<T> setDescription(String identifier, ConceptBaseDescription desc);
}
