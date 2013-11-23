package com.lewuathe.plugins.jenkinsjubatus;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * @author Kai Sasaki
 */

public class JubatusClientTest extends HudsonTestCase {
	
    @Test
    public void testTrain() throws Exception {
		JubatusClient client = new JubatusClient("127.0.0.1", 9199, "test");
		client.train("Success", "Scala");
		client.train("Success", "Java");
		client.train("Success", "C");
		client.train("Success", "Python");
		client.train("Success", "LISP");
		client.train("Success", "Haskell");
		client.train("Success", "Ruby");

		client.train("Failure", "C++");
		client.train("Failure", "Perl");
		client.train("Failure", "F#");
		
		client.predict("Python");
		client.predict("C#");
		client.predict("Clojure");
		client.predict("Perl");
		assertNotNull(client);
    }

	@Test
	public void testConnection() throws Exception {
		JubatusClient client = new JubatusClient("127.0.0.1", 9199, "test");
		assertNotNull(client);
	}

    //TODO write test play path resolved

}
