/*******************************************************************************
 * Copyright (c) 2012,2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package at.srfg.iasset.repository.connectivity.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Configuration;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartFeature;





/**
 * <p>
 * A static factory for creating consumer objects out of <code>@Path</code> annotated interfaces.
 * </p>
 *
 * @see Path
 * @see Provider
 */
public class ConsumerFactory {

  /**
   * <p>
   * Creates a consumer object out of a <code>@Path</code> interface. It will create a proxy object of the interface
   * that calls the specified service using the passed in base url.
   * </p>
   *
   * @param baseUrl The server url hosting the specified service.
   * @param type The <code>@Path</code> annotated interface class object.
   * @return a proxy object for the passed in type.
   */
  public static <T> T createConsumer( String baseUrl, Class<T> type ) {
    return createConsumer( baseUrl, new ClientConfig(), type );
  }
  /**
   * <p>
   * Creates a consumer object out of a <code>@Path</code> interface. It will create a proxy object of the interface
   * that calls the specified service using the passed in base url. The de/serialization is done using the passed in
   * <code>@Provider</code> objects. The passed in {@link Configuration} will be used to create the {@link Client}
   * instances.
   * </p>
   *
   * @param baseUrl The server url hosting the specified service.
   * @param configuration The {@link Configuration} to use for building the {@link Client}.
   * @param type The <code>@Path</code> annotated interface class object.
   * @param customProvider An array of <code>@Provider</code> object for de/serialization.
   * @return a proxy object for the passed in type.
   */
  @SuppressWarnings( "unchecked" )
  public static <T> T createConsumer( String baseUrl, Configuration configuration, Class<T> type ) {
    checkUrl( baseUrl );
    checkType( type );
    checkConfiguration( configuration );
    checkAnnotation( type );
    ensureTypeIsAnInterface( type );
    Path path = type.getAnnotation( Path.class );
    ensureMultiPartFeature( configuration, type );
    return ( T )Proxy.newProxyInstance( type.getClassLoader(),
                                        new Class<?>[] { type },
                                        createHandler( baseUrl, configuration, path ) );
  }

  /**
   * <p>
   * Creates a consumer object out of a <code>@Path</code> interface. It will create a proxy object of the interface
   * that calls the specified service using the passed in base url. The de/serialization is done using the passed in
   * <code>@Provider</code> objects. The passed in {@link Client} will be used to send requests.
   * </p>
   *
   * @param baseUrl The server url hosting the specified service.
   * @param client The {@link Client} to use for sending requests
   * @param type The <code>@Path</code> annotated interface class object.
   * @param customProvider An array of <code>@Provider</code> object for de/serialization.
   * @return a proxy object for the passed in type.
   */
  @SuppressWarnings( "unchecked" )
  public static <T> T createConsumer( String baseUrl, Client client, Class<T> type ) {
    checkUrl( baseUrl );
    checkType( type );
    checkClient( client );
    checkAnnotation( type );
    ensureTypeIsAnInterface( type );
    Path path = type.getAnnotation( Path.class );
    ensureMultiPartFeature( client.getConfiguration(), type );
    return ( T )Proxy.newProxyInstance( type.getClassLoader(),
                                        new Class<?>[] { type },
                                        createHandler( baseUrl, client, path ) );
  }

  private static <T> void ensureMultiPartFeature( Configuration configuration, Class<T> type ) {
    if( hasFormDataParam( type ) ) {
       ( ( ClientConfig )configuration ).register( MultiPartFeature.class );
    }
  }

  private static boolean hasFormDataParam( Class<?> type ) {
    for( Method method : type.getMethods() ) {
      if( ClientHelper.hasFormAnnotation( method, FormDataParam.class ) ) {
        return true;
      }
    }
    return false;
  }

  private static ResourceInvocationHandler createHandler( String baseUrl,
                                                          Configuration configuration,
                                                          Path path )
  {
    return new ResourceInvocationHandler( baseUrl + path.value(), configuration );
  }

  private static ResourceInvocationHandler createHandler( String baseUrl,
                                                          Client client,
                                                          Path path )
  {
    return new ResourceInvocationHandler( baseUrl + path.value(), client );
  }

  private static void checkUrl( String url ) {
    try {
      new URL( url );
    } catch( MalformedURLException invalidUrlException ) {
      throw new IllegalArgumentException( url + " is not a valid url", invalidUrlException );
    }
  }

  private static void checkType( Class<?> type ) {
    if( type == null ) {
      throw new IllegalArgumentException( "type must not be null." );
    }
  }

  private static void checkConfiguration(Configuration configuration) {
    if( configuration == null ) {
      throw new IllegalArgumentException( "Configuration must not be null" );
    }
  }

  private static void checkClient(Client client) {
    if( client == null ) {
      throw new IllegalArgumentException( "Client must not be null" );
    }
  }

  private static void checkAnnotation( Class<?> type ) {
    if( !type.isAnnotationPresent( Path.class ) ) {
      throw new IllegalArgumentException( type.getName() + " is not a Resource. No @Path Annotation." );
    }
  }

  private static void ensureTypeIsAnInterface( Class<?> type ) {
    if( !type.isInterface() ) {
      throw new IllegalArgumentException( type.getName() + " is not an interface. You do not " +
      		                              "want a dependency to cglib, do you?" );
    }
  }

  private ConsumerFactory() {
    // prevent instantiation
  }
}
