package com.wintersteiger.passat.connector.model;

import java.time.LocalDateTime;
import java.util.Random;

import com.wintersteiger.passat.connector.model.aas.AASModelLogic;

import at.srfg.iasset.connector.environment.LocalEnvironment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AAS2RDFLogic implements AASModelLogic{

    @Override
    public void injectLogic(LocalEnvironment environment) {
        // Provide linking to the AAS Model - at this point, she live data from the asset might be provied!
        environment.registerValueCallback("http://example.org/aas2rdf", "http://example.org/aas2rdf/submodel", "data.lastUpdate", () -> LocalDateTime.now());
        environment.registerValueCallback("http://example.org/aas2rdf", "http://example.org/aas2rdf/submodel", "data.maxRotationSpeed", () -> AAS2RDFLogic.nextInt(15000));
    }
    public static int nextInt(int bound) {
        return new Random().nextInt(bound);
    }
}
