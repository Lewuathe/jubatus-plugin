package com.lewuathe.plugins.jenkinsjubatus;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Map;

import us.jubat.classifier.ClassifierClient;
import us.jubat.classifier.EstimateResult;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

public class JubatusClient {
    private final ClassifierClient client;
    private final Random random;

    public JubatusClient(String hostname, int port, String name) throws Exception {
		try {
			this.client = new ClassifierClient(hostname, port, name, 1);
			this.random = new Random(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
    }

    /**
     * Helper function for making Datum object.
     *
     * @param name
     * @return
     */
    private static Datum makeDatum(Map<String, String> data) {
		Datum d = new Datum();
		for (Map.Entry<String, String> e : data.entrySet()) {
			d.addString(e.getKey(), e.getValue());
			System.out.println("key=" + e.getKey() + ", value=" + e.getValue());
		}
		return d;
    }

    private static LabeledDatum makeTrain(String label, Map<String, String> data) {
        return new LabeledDatum(label, makeDatum(data));
    }

    public void train(String label, Map data) {
		LabeledDatum[] trainData = {
			makeTrain(label, data)
		};

        // prepare training data
        // predict the last ones (that are commented out)
        List<LabeledDatum> t = new ArrayList<LabeledDatum>(Arrays.asList(trainData));
        Collections.shuffle(t, random);

        // run train
        client.train(t);
    }

    private static EstimateResult findBestResult(List<EstimateResult> res) {
        EstimateResult best = null;
        for (EstimateResult r : res) {
            if (best == null || best.score < r.score) {
                best = r;
            }
        }
        return best;
    }

    public void predict(Map d) {
        // predict the last shogun
		//        Datum[] data = { makeDatum("慶喜"), makeDatum("義昭"), makeDatum("守時"), };
		Datum[] data = { 
			makeDatum(d)
		};
        for (Datum datum : data) {
            List<List<EstimateResult>> res = client.classify(Arrays.asList(datum));
            // get the predicted shogun name
            System.out.println(findBestResult(res.get(0)).label + "->" + datum.stringValues.get(0).value);
        }
    }
}
