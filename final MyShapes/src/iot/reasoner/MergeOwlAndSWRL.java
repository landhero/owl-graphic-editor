package iot.reasoner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

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
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class MergeOwlAndSWRL {

	public final static String stS = "→";
	public final static String andS = "∧";
	public final static String lbS = "\\(";
	public final static String rbS = "\\)";
	public final static String vS = "\\?";
	public final static String qS = ",";
	
	public static void merge(String orginalOWL, String swrlSource, String destOWL) throws Exception
	{
		//读取 owl 创建本体模型
		// ----- start -----
		File ontofile = new File(orginalOWL);		
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontofile);
        IRI documentIRI = man.getOntologyDocumentIRI(ont);
        String prefix = ont.getOntologyID().getOntologyIRI() + "#";
        System.out.println("\ndoc IRI:"+documentIRI+"\nprefix:"+prefix);
        // ----- end ----- 
        OWLDataFactory dfac = man.getOWLDataFactory();
        
        // 从swrl文件中读取出每一个规则语句
        
        
        // ----- start -----
        File tempfile = new File(swrlSource);
        BufferedReader bbr = new BufferedReader(
				new InputStreamReader(
					new FileInputStream(tempfile)));
		String line = "";
		while((line = bbr.readLine())!=null){
			//文件处理
			setRule(man, ont, dfac, prefix, line);
		}
		bbr.close();
//        // ----- end -----
//         
        File des = new File(destOWL);
        FileOutputStream fos = new FileOutputStream(des);
        man.saveOntology(ont, fos);
        fos.close();
//        
//        
	}
	
	public static void setRule( OWLOntologyManager man, OWLOntology ont, OWLDataFactory dfac, String prefix, String line )
	{
		//直接对句子进行解析
		Set<SWRLAtom> body =  new HashSet<SWRLAtom>();
		Set<SWRLAtom> head = new HashSet<SWRLAtom>();
		
		line = line.replaceAll(" ", "");
		//形如 proterty(?x,?y)^proterty(?z,?m)->property(?x,?y)^property(?u,?v)
		String lines[] = line.split(stS);
		System.out.println("body:"+lines[0]+" head:"+lines[1]);
		
		
		//***** body *****
		//针对每一句话的条件 确定唯一的规则添加进本体中
		//形如 proterty(?x,?y)^proterty(?z,?m)
		String cons[] = lines[0].split(andS);
		putProperty(body, dfac, prefix, cons);
		// ***** head *****
		//对每一句话的结果添加一定的规则放到结果中
		String res[] = lines[1].split(andS);
		putProperty(body, dfac, prefix, cons);
		
		SWRLRule unclerule = dfac.getSWRLRule(body, head);
        //add rule
        man.applyChange(new AddAxiom(ont,unclerule));
	}
	
	public static void putProperty(Set<SWRLAtom> part, OWLDataFactory dfac, String prefix, String[] list )
	{
		for (int i=0; i<list.length; i++)
		{
			//形如 property(?x,?y)
			System.out.println("list "+i+" "+list[i]);
			String temp[] = (list[i]).split(lbS);//以左括号划分
			String Name = temp[0];
			
			
			
			
			temp[1] = temp[1].replaceAll(rbS, "");//去除右括号
			String subtemp[] = temp[1].split(qS);//以逗号划分
			if (subtemp.length == 1)
			{//如果概念包含的元素只有一个 那就是判断
				OWLClassExpression property = dfac.getOWLClass(IRI.create(prefix+Name));
				String s = subtemp[0].replaceAll(vS, "");
				SWRLVariable var = dfac.getSWRLVariable(IRI.create(prefix+s));
				part.add(dfac.getSWRLClassAtom(property, var));
				//这里需要单独寻找对应的方式
			}
			else if (subtemp.length == 2)
			{//如果关系中包含的元素有两个 属性
				String s0 = subtemp[0].replaceAll(vS, "");
				SWRLVariable var0 = dfac.getSWRLVariable(IRI.create(prefix+s0));
				if (subtemp[1].contains(vS))//如果后一个是变量 则为关系属性
				{
					OWLObjectProperty property = dfac.getOWLObjectProperty(IRI.create(prefix+Name));
					String s1 = subtemp[1].replaceAll(vS, "");
					SWRLVariable var1 = dfac.getSWRLVariable(IRI.create(prefix+s1));
					part.add(dfac.getSWRLObjectPropertyAtom(property, var0, var1));
				}
				else // 如果不是关系属性 就是数值属性
				{
					OWLDataProperty property = dfac.getOWLDataProperty(IRI.create(prefix+Name)); 
					String s1 = subtemp[1];
					OWLLiteral owlliteral = dfac.getOWLLiteral(s1);
					SWRLLiteralArgument  var1 = dfac.getSWRLLiteralArgument(owlliteral);
					dfac.getSWRLDataPropertyAtom(property, var0, var1);
					//注意观察 这里需要的getSWRLDataPropertyAtom 需要的参数是 SWRLIArgument 和 SWRLDArgument
				}
			}
		}
	}
}
