package cn.edu.pku.ogeditor.iot;
import java.util.ArrayList;
import java.util.HashMap;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import cn.edu.pku.ogeditor.actions.DeployAction;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


public class IOTAuto {
	private static OntModel model;
	private static final String NS = "http://www.owl-ontologies.com/Ontology_IOT.owl#";

	private static HashMap<String, String> id2type =  new HashMap<String, String>();
	private static ArrayList<String> ids = new ArrayList<String>();
	private static boolean SHOULD_RUN = false;
	public static void iotRun()
	{
		model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC,null);

		//model.read("file:./pizza.owl");
		model.read(DeployAction.filePath);

		System.out.println("start");
		for(String id : ids)
		{
			String type = id2type.get(id);
			OntClass curClass = model.getOntClass(NS + type);
			curClass.createIndividual(NS + id);
			//getProperties
			//setProperties
		}
		
		model.prepare();
		
		ExtendedIterator<Individual> inds = model.listIndividuals();
		while(inds.hasNext())
		{
			Individual ind = inds.next();
			StmtIterator props = ind.listProperties();
			while(props.hasNext())
			{
				Statement prop = props.next();
				IOTUtil.set(prop);
			}
		}
	}
	public static void main(String[] args)
	{
		while(true)
		{
			iotRun();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void deploy()
	{
		setSHOULD_RUN(true);
		new Thread()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(isSHOULD_RUN())
				{
					iotRun();
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}.start();
	}
	public static void stopDeploy()
	{
		setSHOULD_RUN(false);
	}
	public static void setSHOULD_RUN(boolean sHOULD_RUN) {
		SHOULD_RUN = sHOULD_RUN;
	}
	public static boolean isSHOULD_RUN() {
		return SHOULD_RUN;
	}
}
