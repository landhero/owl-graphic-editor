/**
 * 
 */
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
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;



/**
 * @author Velo
 *
 */
public class UncleRule {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

//		ExamplesOWLAPI e = new ExamplesOWLAPI();
//    	e.shouldUseReasoner();
//    	e.shouldCreateAndSaveOntology();
		
		UncleRule ur = new UncleRule();
//		ur.uncleTest();
		ur.uncleTest2();
	}
	
	/*
	 * */
	public void uncleTest() throws Exception{
		
		File ontofile = new File("examples/data/uncle2.owl");
		File f = new File("examples/ruletest1.txt");
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter wos = new OutputStreamWriter(fos);
		
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);
        IRI documentIRI = man.getOntologyDocumentIRI(ont);
        wos.write("\ndoc IRI:"+documentIRI);
        
        String prefix = ont.getOntologyID().getOntologyIRI() + "#";
        wos.write("\nprefix:"+prefix);
       
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
        
        OWLDataFactory dfac = man.getOWLDataFactory();
                
        OWLObjectProperty hasUncle = dfac.getOWLObjectProperty(IRI.create(prefix+"hasUncle"));
        OWLObjectProperty hasFather = dfac.getOWLObjectProperty(IRI.create(prefix+"hasFather"));
        OWLNamedIndividual tom =dfac.getOWLNamedIndividual(IRI.create(prefix+"Tom"));
        wos.write("\nson:"+tom);
        
        reasoner.precomputeInferences();
        
        NodeSet<OWLNamedIndividual> father = reasoner.getObjectPropertyValues(tom, hasFather) ;
        wos.write("\ntom hasFather:"+ father );
                
        boolean consistent = reasoner.isConsistent();
        wos.write("\nConsistent: " + consistent);
        
        NodeSet<OWLNamedIndividual> uncle = 
        		reasoner.getObjectPropertyValues(tom, hasUncle);
        wos.write("\nuncle isEmpty:"+uncle.isEmpty());
        
        
        Set<OWLAxiom> abox = ont.getABoxAxioms(true);
        Set<OWLAxiom> tbox = ont.getTBoxAxioms(true);
        Set<OWLAxiom> rbox = ont.getRBoxAxioms(true);
        
        wos.write("\n\nabox:");
        for(Iterator<OWLAxiom> it = abox.iterator(); it.hasNext();){
        	wos.write("\n"+it.next().toString());
        }
        
        wos.write("\n\ntbox:");
        for(Iterator<OWLAxiom> it = tbox.iterator(); it.hasNext();){
        	wos.write("\n"+it.next().toString());
        }
        
        wos.write("\n\nrbox:");
        wos.write((rbox.isEmpty())? "empty" : "not empty");
        for(Iterator<OWLAxiom> it = rbox.iterator(); it.hasNext();){
        	wos.write("\n"+it.next().toString());
        }
        wos.write("\n<---END--->");    
        wos.close();
        System.out.println("output finisned.");
	}
	
	/*
	 * save unclerule:
	 * hasFather(?x,?y) conjunction hasBrother(?y,?z) --> hasUncle(?x,?z)  
	 * */
	public void uncleTest2() throws Exception{
		File ontofile = new File("examples/uncle3.owl");		
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);
        IRI documentIRI = man.getOntologyDocumentIRI(ont);
        String prefix = ont.getOntologyID().getOntologyIRI() + "#";
        System.out.println("\ndoc IRI:"+documentIRI+"\nprefix:"+prefix);
            
        OWLDataFactory dfac = man.getOWLDataFactory();
        
        //get object properties
        OWLClass person = dfac.getOWLClass(IRI.create(prefix+"Person"));
        OWLObjectProperty hasUncle = dfac.getOWLObjectProperty(IRI.create(prefix+"hasUncle"));
        OWLObjectProperty hasFather = dfac.getOWLObjectProperty(IRI.create(prefix+"hasFather"));
        OWLObjectProperty hasBrother = dfac.getOWLObjectProperty(IRI.create(prefix+"hasBrother"));
        
        //define rule
        SWRLVariable x = dfac.getSWRLVariable(IRI.create(prefix+"x")); //son
        SWRLVariable y = dfac.getSWRLVariable(IRI.create(prefix+"y")); //father
        SWRLVariable z = dfac.getSWRLVariable(IRI.create(prefix+"z")); //uncle
        
        Set<SWRLAtom> body =  new HashSet<SWRLAtom>();
        	body.add(dfac.getSWRLObjectPropertyAtom(hasFather, x, y)); 
        	body.add(dfac.getSWRLObjectPropertyAtom(hasBrother, y, z));
        Set<SWRLAtom> head = new HashSet<SWRLAtom>();
        	head.add(dfac.getSWRLObjectPropertyAtom(hasUncle, x, z));
        
        SWRLRule unclerule = dfac.getSWRLRule(body, head);
        //add rule
        man.applyChange(new AddAxiom(ont,unclerule));
               
        //try to reason: who is tom's uncle?
        OWLNamedIndividual tom = dfac.getOWLNamedIndividual(IRI.create(prefix+"Tom"));
        //add an uncle for tom
        OWLNamedIndividual ben = dfac.getOWLNamedIndividual(IRI.create(prefix+"Ben"));
//        dfac.getOWLDataPropertyAssertionAxiom(arg0, arg1, arg2)
        OWLAxiom newuncle = dfac.getOWLObjectPropertyAssertionAxiom(hasUncle, tom, ben);
        man.applyChange(new AddAxiom(ont,dfac.getOWLClassAssertionAxiom(person, ben)));
        man.applyChange(new AddAxiom(ont,newuncle));
      //save ontology, default format:RDFXML
        File swrlf = new File("examples/uncle.swrl.owl");
        FileOutputStream swrlfos = new FileOutputStream(swrlf);
        man.saveOntology(ont, swrlfos);
        swrlfos.close();
        
        //should output:Ben
        System.out.println("\nBefore reasoning-Tom's uncle:\n"
        		+tom.getObjectPropertyValues(hasUncle, ont));
        
        //start reasoner
        PelletReasoner pReasoner = PelletReasonerFactory.getInstance().createReasoner(ont);
        pReasoner.getKB().realize(); 
//        pReasoner.getKB().printClassTree();
//        pReasoner.getInstances(person, false);
        //should output:Ben,Jack,John
        System.out.println("\nAfter-reasoning-Tom's Uncle:\n"
        		+pReasoner.getObjectPropertyValues(tom, hasUncle));
        
        //save result
        File r = new File("examples/ruleinfer.txt");
        FileOutputStream rf = new FileOutputStream(r);
        OutputStreamWriter rfw = new OutputStreamWriter(rf);
        rfw.write("\nTom's Uncle:\n");
        NodeSet<OWLNamedIndividual> s = pReasoner.getObjectPropertyValues(tom, hasUncle);
        for (Iterator<Node<OWLNamedIndividual>> it = s.iterator(); it.hasNext(); ){
        	rfw.write(it.next().getEntities().toString()+"\n");
        }
        rfw.write("\n<---END--->");
        rfw.close();
	}

}
