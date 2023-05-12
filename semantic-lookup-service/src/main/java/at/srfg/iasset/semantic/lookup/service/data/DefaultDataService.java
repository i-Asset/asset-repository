package at.srfg.iasset.semantic.lookup.service.data;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.srfg.iasset.semantic.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptClassRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptRepository;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassProperty;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;
import at.srfg.iasset.semantic.model.DataTypeEnum;
import jakarta.annotation.PostConstruct;

@Service
public class DefaultDataService {

	@Autowired
	private ConceptClassRepository ccRepo;
	@Autowired
	private ConceptRepository<ConceptProperty> propRepo;
	@Autowired
	private ConceptRepository<ConceptPropertyValue> valueRepo;
	@Autowired
	private ConceptClassPropertyRepository ccPropRepo;

	@PostConstruct
	@Transactional
	protected void init() {
		// 
		Optional<ConceptClass> optCauseOfErrorClass = ccRepo.findByConceptId("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry");
		ConceptClass causeOfError = optCauseOfErrorClass.orElseGet(new Supplier<ConceptClass>() {

			@Override
			public ConceptClass get() {
				ConceptClass cc = new ConceptClass("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry");
				cc.setPreferredLabel(Locale.ENGLISH, "Concept Class defining the Table for Error Causes");
				cc.setPreferredLabel(Locale.GERMAN, "Concept Class zur Defintion der Störursachentabelle");
				cc.setCategory("cmms");
				cc.setShortName("causeOfErrorEntry");
				return ccRepo.save(cc);
			}
		});
		Optional<ConceptProperty> optErrorCode = propRepo.findByConceptId("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry/code");
		ConceptProperty errorCode = optErrorCode.orElseGet(new Supplier<ConceptProperty>() {

			@Override
			public ConceptProperty get() {
				ConceptProperty prop = new ConceptProperty("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry/code");
				prop.setPreferredLabel(Locale.GERMAN, "Fehler-Code");
				prop.setPreferredLabel(Locale.ENGLISH, "Error-Code");
				prop.setCategory("cmms");
				prop.setShortName("code");
				prop.setDataType(DataTypeEnum.STRING);
				return propRepo.save(prop);
			}
		});
		Optional<ConceptProperty> optErrorText = propRepo.findByConceptId("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry/description");
		ConceptProperty errorDescription = optErrorText.orElseGet(new Supplier<ConceptProperty>() {

			@Override
			public ConceptProperty get() {
				ConceptProperty prop = new ConceptProperty("http://iasset.salzburgresearch.at/cmms/causeOfErrorEntry/description");
				prop.setPreferredLabel(Locale.GERMAN, "Beschreibung Fehler-Code");
				prop.setPreferredLabel(Locale.ENGLISH, "Description Error-Code");
				prop.setCategory("cmms");
				prop.setShortName("description");
				prop.setDataType(DataTypeEnum.STRING);
				return propRepo.save(prop);
			}
		});
		ccPropRepo.findByConceptClassAndProperty(causeOfError, errorCode)
			.orElseGet(new Supplier<ConceptClassProperty>() {
	
				@Override
				public ConceptClassProperty get() {
					ConceptClassProperty p1 = new ConceptClassProperty(causeOfError, errorCode);
					return ccPropRepo.save(p1);
				}
			});
		//
		ccPropRepo.findByConceptClassAndProperty(causeOfError, errorDescription)
			.orElseGet(new Supplier<ConceptClassProperty>() {
	
				@Override
				public ConceptClassProperty get() {
					ConceptClassProperty p = new ConceptClassProperty(causeOfError, errorDescription);
					return ccPropRepo.save(p);
				}
			});


		// 
		Optional<ConceptClass> optMessagingClass = ccRepo.findByConceptId("http://iasset.salzburgresearch.at/data/messageBroker");
		ConceptClass messagingClass = optMessagingClass.orElseGet(new Supplier<ConceptClass>() {

			@Override
			public ConceptClass get() {
				ConceptClass cc = new ConceptClass("http://iasset.salzburgresearch.at/data/messageBroker");
				cc.setPreferredLabel(Locale.ENGLISH, "Concept Class defining the EventBroker Configuration");
				cc.setPreferredLabel(Locale.GERMAN, "Concept Class zur Defintion von Event Einstellungen (Broker)");
				cc.setCategory("iAsset");
				cc.setShortName("messageBroker");
				return ccRepo.save(cc);
			}
		});
		Optional<ConceptProperty> optHosts = propRepo.findByConceptId("http://iasset.salzburgresearch.at/data/messageBroker/hosts");
		ConceptProperty hosts = optHosts.orElseGet(new Supplier<ConceptProperty>() {

			@Override
			public ConceptProperty get() {
				ConceptProperty prop = new ConceptProperty("http://iasset.salzburgresearch.at/data/messageBroker/hosts");
				prop.setPreferredLabel(Locale.GERMAN, "Host-Adressen für Event Broker settings");
				prop.setPreferredLabel(Locale.ENGLISH, "Host-Names for Event Broker");
				prop.setCategory("iAsset");
				prop.setShortName("hosts");
				prop.setDataType(DataTypeEnum.STRING);
				return propRepo.save(prop);
			}
		});
		Optional<ConceptPropertyValue> optKafka = valueRepo.findByConceptId("http://iasset.salzburgresearch.at/data/messageBroker/brokerType/kafka");
		ConceptPropertyValue kafka = optKafka.orElseGet(new Supplier<ConceptPropertyValue>() {
			
			@Override
			public ConceptPropertyValue get() {
				ConceptPropertyValue kafka = new ConceptPropertyValue("http://iasset.salzburgresearch.at/data/messageBroker/brokerType/kafka");
				kafka.setPreferredLabel(Locale.ENGLISH, "Apache Kafka");
				kafka.setValue("KAFKA");
				kafka.setDataType(DataTypeEnum.STRING);
				return valueRepo.save(kafka);
			}
		});

		Optional<ConceptPropertyValue> optMQTT = valueRepo.findByConceptId("http://iasset.salzburgresearch.at/data/messageBroker/brokerType/mqtt");
		ConceptPropertyValue mqtt = optMQTT.orElseGet(new Supplier<ConceptPropertyValue>() {
			
			@Override
			public ConceptPropertyValue get() {
				ConceptPropertyValue value = new ConceptPropertyValue("http://iasset.salzburgresearch.at/data/messageBroker/brokerType/mqtt");
				value.setPreferredLabel(Locale.ENGLISH, "Message Queuing Telemetry Transport");
				value.setValue("MQTT");
				value.setDataType(DataTypeEnum.STRING);
				return valueRepo.save(value);
			}
		});
		

		Optional<ConceptProperty> optBrokerType = propRepo.findByConceptId("http://iasset.salzburgresearch.at/data/messageBroker/brokerType");
		ConceptProperty brokerType = optBrokerType.orElseGet(new Supplier<ConceptProperty>() {

			@Override
			public ConceptProperty get() {
				ConceptProperty prop = new ConceptProperty("http://iasset.salzburgresearch.at/data/messageBroker/brokerType");
				prop.setPreferredLabel(Locale.GERMAN, "Messaging Infrastruktur");
				prop.setPreferredLabel(Locale.ENGLISH, "Messaging Infrastructure");
				prop.setCategory("iAsset");
				prop.setShortName("brokerType");
				
				prop.setDataType(DataTypeEnum.STRING);
				
				prop = propRepo.save(prop);
				prop.addPropertyValue(mqtt);
				prop.addPropertyValue(kafka);
				return propRepo.save(prop);
			}
		});
		//
		ccPropRepo.findByConceptClassAndProperty(messagingClass, brokerType)
				.orElseGet(new Supplier<ConceptClassProperty>() {

					@Override
					public ConceptClassProperty get() {
						ConceptClassProperty p1 = new ConceptClassProperty(messagingClass, brokerType);
						return ccPropRepo.save(p1);
					}
				});
		//
		ccPropRepo.findByConceptClassAndProperty(messagingClass, hosts)
				.orElseGet(new Supplier<ConceptClassProperty>() {

					@Override
					public ConceptClassProperty get() {
						ConceptClassProperty p = new ConceptClassProperty(messagingClass, hosts);
						return ccPropRepo.save(p);
					}
				});
		
	}
}
