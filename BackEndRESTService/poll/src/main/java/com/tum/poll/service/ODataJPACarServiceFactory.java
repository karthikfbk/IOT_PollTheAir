/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package com.tum.poll.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;


import com.tum.poll.model.Gasindex;
import com.tum.poll.model.Geolocation;
import com.tum.poll.model.Pollutiondata;

public class ODataJPACarServiceFactory extends PollODataServiceFactory {

	private static final int PAGE_SIZE = 5;

	
	static {
		/**List<Car> cars = new ArrayList<Car>();
		Calendar mfDate = Calendar.getInstance();
		mfDate.set(2013, 02, 01);
		Address address = new Address("S1", "C1", "Z1", "CN");

		Manufacturer mf = new Manufacturer(1, "SuperCar", mfDate, address, cars);

		Calendar bDate = Calendar.getInstance();
		mfDate.set(1980, 02, 19);
		Driver driver = new Driver(1L, "Speeder", "Super", "Bolt", null, bDate);

		Key key = new Key(1, 2);
		Calendar carDate = Calendar.getInstance();
		carDate.set(2014, 02, 20);
		Car car = new Car(key, "M1", 20000.0, 2014, carDate.getTime(), mf,
				driver);
		cars.add(car);
		driver.setCar(car);


		List<Gasindex> gases = new ArrayList<Gasindex>();
		
		Geolocation location = new Geolocation(1L,"Munich",48.1351253,11.5819806,gases);
		Gasindex gas1 = new Gasindex(1L, 48.1351253,11.5819806,"CO2",205.30,"NO2",101.2,location);
		
		Gasindex gas2 = new Gasindex(2L,48.1351253,11.5819806,"CO2",305.30,"NO2",201.2,location);
		gases.add(gas1);
		gases.add(gas2);
		
		List<Gasindex> gases1 = new ArrayList<Gasindex>();
		Geolocation location2 = new Geolocation(2L,"Paris",48.856614,2.3522219,gases1);
		Gasindex gas11 = new Gasindex(3L,48.856614,2.3522219,"CO2",5.30,"NO2",51.2,location2);
		
		Gasindex gas22 = new Gasindex(4L,48.856614,2.3522219,"CO2",65.30,"NO2",1.2,location2);
		gases1.add(gas11);
		gases1.add(gas22);
		**/
		Calendar mfDate = Calendar.getInstance();
		Pollutiondata pol = new Pollutiondata(1L,"Paris",48.856614,2.3522219,500L,mfDate);
		
		//Pollutiondata pol2 = new Pollutiondata(2L,"Munich",48.1351253,11.5819806,150L,mfDate);
		EntityManager em = EmfHolder.createInstance().createEntityManager();
		
		em.getTransaction().begin();
		em.persist(pol);
		//em.persist(pol2);
		em.getTransaction().commit();

	}

	@Override
	public ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = getODataJPAContext();
		EmfHolder emfHolder = EmfHolder.createInstance();
		oDataJPAContext.setEntityManagerFactory(emfHolder.getEntityManagerFactory());
		oDataJPAContext.setPersistenceUnitName(emfHolder.getPersistenceUnitName());

		oDataJPAContext.setPageSize(PAGE_SIZE);
		setDetailErrors(true);

		return oDataJPAContext;
	}

	/**
	 *
	 */
	private static class EmfHolder {
		private static final String PUNIT_NAME = "MyFormulaHsqlDb";
		private static final String PUNIT_NAME_DEFAULT = "MyFormulaJeeDb";
		private static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/DefaultDB";

		final private EntityManagerFactory emf;
		final private String persistenceUnitName;

		private EmfHolder(EntityManagerFactory emf, String unitName) {
			this.emf = emf;
			this.persistenceUnitName = unitName;
		}

		public EntityManagerFactory getEntityManagerFactory() {
			return emf;
		}

		public String getPersistenceUnitName() {
			return persistenceUnitName;
		}

		public static EntityManager createEntityManager() {
			return createInstance().getEntityManagerFactory().createEntityManager();
		}


		public static synchronized EmfHolder createInstance() {
			try {
				InitialContext ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(DATA_SOURCE_NAME);
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
				EntityManagerFactory emf =
						Persistence.createEntityManagerFactory(PUNIT_NAME_DEFAULT, properties);
				emf.createEntityManager();
				return new EmfHolder(emf, PUNIT_NAME_DEFAULT);
			} catch (javax.persistence.PersistenceException e) {
				return new EmfHolder(Persistence.createEntityManagerFactory(PUNIT_NAME), PUNIT_NAME);
			} catch (NamingException e) {
				return new EmfHolder(Persistence.createEntityManagerFactory(PUNIT_NAME), PUNIT_NAME);
			}
		}
	}
}
