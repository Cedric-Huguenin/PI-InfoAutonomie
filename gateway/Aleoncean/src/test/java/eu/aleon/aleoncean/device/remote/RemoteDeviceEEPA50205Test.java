/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Thomas Stezaly - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device.remote;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA50205;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;

/**
 * @author Thomas Stezaly (thomas.stezaly@aleuon.eu)
 *
 */
public class RemoteDeviceEEPA50205Test {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetGetTemperature() {

        final double temperatureSet = 20.0;
        final UserDataEEPA50205 userData = new UserDataEEPA50205();

        try {
            userData.setTemperature(temperatureSet);
            userData.setTeachIn(false);
        } catch (final UserDataScaleValueException e) {
            fail("Throws exception: UserDataScaleValueException");
        }

        final RadioPacket4BS packet = userData.generateRadioPacket();
        final RemoteDeviceEEPA50205 testDevice = new RemoteDeviceEEPA50205(null, packet.getSenderId(), packet.getDestinationId());

        testDevice.parseRadioPacket(packet);

        final double temperatureGet = testDevice.getTemperature();
        final double delta = temperatureGet - temperatureSet;

        System.out.println("temperatureSet: "+temperatureSet);
        System.out.println("temperatureGet: "+temperatureGet);

        // We use a delta of 0.2, because one range point fits to 0.15 degree.
        // So is this value acceptable
        assertTrue(delta < 0.2);
    }
}
