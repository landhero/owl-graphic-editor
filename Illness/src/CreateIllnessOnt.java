import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Stack;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


public class CreateIllnessOnt {
	private static final int LIMIT = 4;
	private static final String SEPARATOR = "\t";
	private static final String MINUS_SEPARATOR = "-";
	private static final String DOT_SEPARATOR = "\\.";

	private static final String NS = "http://icd-10/illness#";
	private static final String PS = "http://icd-10/illness/remark#";

	private static HashMap<String, String> map = new HashMap<String, String>();
	private static HashMap<String, Long> names = new HashMap<String, Long>();
	public static void main(String [] args)
	{
		Stack<String> stack = new Stack<String>();
		File file = new File("ICD-10_1.txt");
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		DatatypeProperty remark = ontModel.createDatatypeProperty(PS);
		//remark.add

		try {
			if(file.exists())
			{
				System.out.println("icd exist");
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while(null != (line = bufferedReader.readLine()))
			{
				//System.out.println(line);
				String[] strings = line.split(SEPARATOR, LIMIT);
				Long value = names.get(strings[2]);
				if(null != value)
					continue;
				names.put(strings[2], new Long(1));
				while(!stack.isEmpty())
				{
					String parString = stack.peek();
					if(isInRange(strings[1],parString))
					{
						//stack.push(strings[1]);
						//map.put(strings[1], strings[2]);
						OntClass curClass = ontModel.createClass(NS + strings[2]);
						curClass.addComment(strings[3],null);
						OntClass parClass = ontModel.getOntClass(NS + map.get(parString));
						//parClass.addSubClass(curClass);
						curClass.setSuperClass(parClass);
						
						break;
					}
					else
					{
						stack.pop();
					}
				}
				if(stack.isEmpty())
				{
					ontModel.createClass(NS + strings[2]).addComment(strings[3], null);
				}
				stack.push(strings[1]);
				map.put(strings[1], strings[2]);
				
//				System.out.println(line);
//				String[] strings = line.split(SEPARATOR, LIMIT);
//				while(!stack.isEmpty())
//				{
//					String parString = stack.peek();
//					if(isInRange(strings[1],parString))
//					{
//						OntClass curClass = ontModel.createClass(NS + strings[1]);
//						OntClass parClass = ontModel.getOntClass(NS + parString);
//						curClass.setSuperClass(parClass);
//						break;
//					}
//					else
//					{
//						stack.pop();
//					}
//				}
//				if(stack.isEmpty())
//					ontModel.createClass(NS + strings[1]);
//				stack.push(strings[1]);
				
			}
			
			String filepath = "illness.owl";
			FileOutputStream out = new FileOutputStream(filepath);
	        OutputStreamWriter writer=new OutputStreamWriter(out, "gb2312") ;

			ontModel.write(writer,"RDF/XML-ABBREV");
			System.out.println("finish");
			
			OntClass tmpClass = ontModel.getOntClass(NS + "霍乱");
			printClasses(tmpClass.listSuperClasses());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void printClasses(ExtendedIterator<?> classes)
	{
		while(classes.hasNext())
		{
			System.out.println(classes.next().toString());
		}
	}
	private static boolean isInRange(String s1, String s2) {
		if(isMinusFormat(s1) && isMinusFormat(s2))
		{
			String[] strs1 = s1.split(MINUS_SEPARATOR);
			String[] strs2 = s2.split(MINUS_SEPARATOR);
			if(strs1[0].compareTo(strs2[0]) >= 0 && strs1[1].compareTo(strs2[1]) <= 0)
			{
				return true;
			}
		}
		else if(isNormalFormat(s1) && isMinusFormat(s2))
		{
			String[] strs2 = s2.split(MINUS_SEPARATOR);
			if(s1.compareTo(strs2[0]) >= 0 && s1.compareTo(strs2[1]) <= 0)
			{
				return true;
			}
		}
		//应该不会遇到
		else if(isDotFormat(s1) && isMinusFormat(s2))
		{
			System.out.println("in Here,not possible!");
			String[] strs1 = s1.split(DOT_SEPARATOR);
			String[] strs2 = s2.split(MINUS_SEPARATOR);
			if(strs1[0].compareTo(strs2[0]) >= 0 && strs1[0].compareTo(strs2[1]) <= 0)
			{
				return true;
			}
		}
		else if(isDotFormat(s1) && isNormalFormat(s2))
		{
			String[] strs1 = s1.split(DOT_SEPARATOR);
			if(strs1[0].equals(s2))
			{
				return true;
			}
		}
		return false;
	}
	private static boolean isNormalFormat(String s) {
		String[] str = s.split(DOT_SEPARATOR);
		if(str.length > 1)
			return false;
		
		str = s.split(MINUS_SEPARATOR);
		if(str.length > 1)
			return false;
		return true;
	}
	private static boolean isDotFormat(String s) {
		String[] str = s.split(DOT_SEPARATOR);
		if(str.length > 1)
			return true;
		return false;
	}
	private static boolean isMinusFormat(String s) {
		String[] str = s.split(MINUS_SEPARATOR);
		if(str.length > 1)
			return true;
		return false;
	}
}
