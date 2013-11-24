package com.lewuathe.plugins.jenkinsjubatus;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;
import java.util.*;

/**
 * @author Kai Sasaki
 */

public class JubatusClientTest extends HudsonTestCase {
	
    @Test
    public void testTrain() throws Exception {
		try {
			JubatusClient client = new JubatusClient("127.0.0.1", 9199, "test");
			Map m = new HashMap<String, String>();
			m.put("lang", "Scala");
			client.train("Success", m);
			m.put("lang", "Java");
			client.train("Success", m);
			m.put("lang", "Perl");
			client.train("Failure", m);
			client.predict(m);
			assertNotNull(client);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Test
	public void testConnection() throws Exception {
		JubatusClient client = new JubatusClient("127.0.0.1", 9199, "test");
		assertNotNull(client);
	}

    //TODO write test play path resolved

}
