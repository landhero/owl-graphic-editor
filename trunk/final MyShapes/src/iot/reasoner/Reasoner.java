package iot.reasoner;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class Reasoner {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Reasoner r = new Reasoner();
		OWLOntologyManager man = null;
		String filepath = "D:\\Program Files (x86)\\eclipse\\tmp\\uncle.swrl.owl";
		try{
			r.testConsistent(filepath);

		}catch(InconsistentOntologyException e){
			System.out.print(e.getCause());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static OWLOntologyManager testConsistent(String filepath) 
		throws OWLOntologyCreationException,
		ReasonerInterruptedException,InconsistentOntologyException{
		//onto file
//		File ontofile = new File("examples/IOT0415.owl");
		File ontofile = new File(filepath);
		
		//onto manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);
		OWLDataFactory dfac = man.getOWLDataFactory();
		IRI docIRI = man.getOntologyDocumentIRI(ont);
		String prefix = ont.getOntologyID().getOntologyIRI()+"#";
		System.out.println("\ndocIRI: " + docIRI +"\nprefix: " + prefix);
		
		//reasoner
		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);

		
		//consistency
		if(reasoner.isConsistent()){
			System.out.println("\nOntoloy is consistent");
			//run swrl rules
			reasoner.getKB().realize();
			
//			OWLNamedIndividual airCondition1 = dfac.getOWLNamedIndividual( IRI.create(prefix + "Air_Condition_1"));
//			OWLDataProperty temperature  = dfac.getOWLDataProperty( IRI.create(prefix + "Air_Condition_Temperature"));
//			Set<OWLLiteral> temps = new HashSet<OWLLiteral>(airCondition1.getDataPropertyValues(temperature, ont));
//			System.out.println("\nair condition 1 - temp: " + temps);
			return man;
			
		}else{
			//how to deal with inconsistency? detailed error info
			System.out.println("\nERROR: inconsistent");
			System.out.println(reasoner.getUnsatisfiableClasses());
			return null;
		}

	}

}
