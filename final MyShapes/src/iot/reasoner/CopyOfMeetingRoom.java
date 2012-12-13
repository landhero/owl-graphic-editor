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

public class CopyOfMeetingRoom {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CopyOfMeetingRoom r = new CopyOfMeetingRoom();
		try{
			r.reasonTest();
		}catch(InconsistentOntologyException e){
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
//		File ontofile = new File("examples/room_norestriction.owl");
		File ontofile = new File("D:\\Program Files (x86)\\eclipse\\tmp/room.owl");
		System.out.println(ontofile.getAbsolutePath());
		
		//onto manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);
		OWLDataFactory dfac = man.getOWLDataFactory();
		IRI docIRI = man.getOntologyDocumentIRI(ont);
		String prefix = ont.getOntologyID().getOntologyIRI()+"#";
		System.out.println("\ndocIRI: " + docIRI +"\nprefix: " + prefix);
		
		OWLNamedIndividual light1 = dfac.getOWLNamedIndividual( IRI.create(prefix + "Front_Light_1"));
		OWLNamedIndividual light11 = dfac.getOWLNamedIndividual(IRI.create(prefix + "Rear_Light_1"));
		OWLNamedIndividual aircon1 = dfac.getOWLNamedIndividual( IRI.create(prefix + "Air_Condition_2"));
		OWLDataProperty isOn = dfac.getOWLDataProperty( IRI.create(prefix + "isOn"));
		OWLDataProperty temper = dfac.getOWLDataProperty(IRI.create(prefix + "Air_Condition_Temperature"));
		printvalue(ont,light1,isOn);
		printvalue(ont,light11,isOn);
		printvalue(ont,aircon1,isOn);
		printvalue(ont,aircon1,temper);
		
		//reasoner
		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner(ont); 
//		PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);
		
		//consistency
		if(reasoner.isConsistent()){
			System.out.println("\nOntoloy is consistent.\nStart reasoning");
			//run swrl rules
			reasoner.getKB().realize();
			//projector is on,so light is off
			printvalue(ont,light1,isOn,reasoner);
			printvalue(ont,light11,isOn,reasoner);
			printvalue(ont,aircon1,isOn,reasoner);
			printvalue(ont,aircon1,temper,reasoner);
		}else{
			//how to deal with inconsistency? detailed error info
			System.out.println("\nERROR: inconsistent");
			System.out.println(reasoner.getUnsatisfiableClasses());
			
		}

	}
	public void printvalue(OWLOntology ont,OWLNamedIndividual ind,OWLDataProperty dpro){
		System.out.print("\n"+ind.getIRI().getFragment()+"--"+ dpro.getIRI().getFragment()+":");
		Set<OWLLiteral> origin = ind.getDataPropertyValues(dpro, ont);
		System.out.println(origin);
	}
	
	public void printvalue(OWLOntology ont,OWLNamedIndividual ind,OWLDataProperty dpro,PelletReasoner reasoner ){
		System.out.print("\n"+ind.getIRI().getFragment()+"--"+ dpro.getIRI().getFragment()+":");
		
		Set<OWLLiteral> origin = ind.getDataPropertyValues(dpro, ont);
		Set<OWLLiteral> infer =  reasoner.getDataPropertyValues(ind, dpro);
		
		if (origin.isEmpty())
			System.out.println(infer);
		else{
			
			//print infer - origin
			for (Iterator<OWLLiteral> it = origin.iterator();it.hasNext();){
				OWLLiteral temp = it.next();
				if(infer.contains(temp)){
					infer.remove(temp);
				}
			}
			System.out.println(infer);		
		}
	}

}
