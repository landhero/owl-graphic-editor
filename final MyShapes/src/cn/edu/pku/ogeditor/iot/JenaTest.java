package cn.edu.pku.ogeditor.iot;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class JenaTest {
	private static String ontString = "http://www.owl-ontologies.com/Ontology_IOT.owl#";
	private static OntModel model;

	public static void main(String[] args) {
		model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		//model.read("file:./pizza.owl");
		model.read("file:./IOT0415.owl");

		System.out.println("start");
		
		ExtendedIterator<OntClass> classes = model.listNamedClasses();
		//initClassesNames();
		printClasses(classes);
		System.out.println("seprator1");
		
		ExtendedIterator<ObjectProperty> objectPro = model.listObjectProperties();
		printClasses(objectPro);
		System.out.println("seprator2");
		
		ExtendedIterator<DatatypeProperty> datatypePro = model.listDatatypeProperties();
		printClasses(datatypePro);
		System.out.println("separator3---------------------------------------------------------");
		
		ExtendedIterator<Individual> individuals = model.listIndividuals();
		printClasses(individuals);
		
		System.out.println("end");
		
		OntClass room = model.getOntClass(ontString + "Room");
		if(null != room)
			System.out.println("room exist");
		//StmtIterator roomProp = room.listProperties();
		//printClasses(roomProp);
		//room.createIndividual(ontString + "Room1");
		Individual room1 = model.getIndividual(ontString + "Room_1");
		if(null != room1)
		{
			System.out.println("room1 exist");
		}
		printClasses(room1.listProperties());
		Statement firstProp = room1.listProperties().next();
		System.out.println(firstProp.getPredicate() + " : " + firstProp.getObject()) ;
		
		
		ExtendedIterator<OntClass> superClasses = room.listSuperClasses();
		//printClasses(superClasses);
		System.out.println("-------------------------------------------------------------------");

		StmtIterator roomProp = room.listProperties();
		printClasses(roomProp);
		
		printClasses(room.listInstances());
		System.out.println("-------------------------------------------------------------------");

		room1 = model.getIndividual("http://www.owl-ontologies.com/Ontology_IOT.owl#Room_1");
		
		System.out.println("-------------------------------------------------------------------");
		DatatypeProperty isOccupied = model.getDatatypeProperty(ontString + "isOccupied");
		if(null != isOccupied)
		{
			System.out.println("isOccupied exist");
		}
		OntResource domain = isOccupied.getDomain();
		System.out.println(domain.getURI());
		
	}
	
	public static void printClasses(ExtendedIterator<?> classes)
	{
		while(classes.hasNext())
		{
			System.out.println(classes.next().toString());
		}
	}
}
