package iot.reasoner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import org.mindswap.pellet.ABox;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class MeetingRoom {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MeetingRoom r = new MeetingRoom();
		try {
			r.reasonTest();
		} catch (InconsistentOntologyException e) {
			System.out.print(e.getCause());
		}

	}

	public void reasonTest() 
		throws OWLOntologyCreationException,
		ReasonerInterruptedException,InconsistentOntologyException{
		//onto file
//		File ontofile = new File("examples/IOT0415.owl");
//		File ontofile = new File("examples/IOT0415_onerule.owl"); 
//		File ontofile = new File("examples/room_test.owl"); //with cardinality restriction
		File ontofile = new File("D:\\Program Files (x86)\\eclipse\\tmp/room_norestriction.owl");
		
		//onto manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);

		OWLDataFactory dfac = man.getOWLDataFactory();
		IRI docIRI = man.getOntologyDocumentIRI(ont);
		String prefix = ont.getOntologyID().getOntologyIRI()+"#";
		System.out.println("\ndocIRI: " + docIRI +"\nprefix: " + prefix);
		
		OWLNamedIndividual light1 = dfac.getOWLNamedIndividual( IRI.create(prefix + "Front_Light_1"));
		OWLNamedIndividual aircon1 = dfac.getOWLNamedIndividual( IRI.create(prefix + "Air_Condition_1"));
		OWLDataProperty isOn = dfac.getOWLDataProperty( IRI.create(prefix + "isOn"));
		OWLDataProperty temper = dfac.getOWLDataProperty(IRI.create(prefix + "Air_Condition_Temperature"));
		
//		man.applyChange(new AddAxiom(ont,dfac.getOWLDataPropertyAssertionAxiom(isOn, light1, true)));
		

		System.out.println("\nBefore reasoning:"
				+"\nlight1 " + isOn.toString()+" : " + light1.getDataPropertyValues(isOn, ont)
				+"\naircon1 " + isOn.toString()+" : " + aircon1.getDataPropertyValues(isOn, ont)
				+"\naircon1 " + temper.toString()+" : " + aircon1.getDataPropertyValues(temper, ont));
		
		//reasoner
		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);		
		//consistency
		if(reasoner.isConsistent()){
			System.out.println("\nOntoloy is consistent.\nStart reasoning");
			//run swrl rules
			reasoner.getKB().realize();
			//projector is on,so light is off
			System.out.println("\nBefore reasoning:"
					+"\nlight1: " + reasoner.getDataPropertyValues(light1, isOn)
					+"\naircon1: " + reasoner.getDataPropertyValues(aircon1, isOn)
					+"\naircon1: " + reasoner.getDataPropertyValues(aircon1, temper));	
		}else{
			//how to deal with inconsistency? detailed error info
			System.out.println("\nERROR: inconsistent");
			System.out.println(reasoner.getUnsatisfiableClasses());
			
		}
		
		Set<OWLLiteral> set = reasoner.getDataPropertyValues(aircon1, temper);
		Iterator<OWLLiteral> iter = set.iterator();
		while(iter.hasNext())
		{
			OWLLiteral li = iter.next();
			System.out.println(li.getDatatype().isInteger());
			System.out.println(li.toString());
		}
	}
}
