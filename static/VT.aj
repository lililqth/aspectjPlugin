import java.io.*;

public aspect aspectjanalysictempfile {
	
	String filename = "ValueTracker.xml";
	
	pointcut HelloWorldPointCut() : execution(* *(..));
	pointcut ValueChangePointCut() : set(* *.*);
	
	before() : HelloWorldPointCut(){
		FileWriter fw = null;
		fw = new FileWriter(filename, true);
		String startString = "<functionstart>" + thisJoinPoint.getSignature().toLongString() +"</functionstart>\n";
		fw.write(startString);
		fw.close();
	}
	
	after() : HelloWorldPointCut(){
		FileWriter fw = null;
		fw = new FileWriter(filename, true);
		String endString = "<functionend>" + thisJoinPoint.getSignature().toLongString() +"</functionend>\n";
		fw.write(endString);
		fw.close();
	}
	
	after() : ValueChangePointCutthrows {
		FileWriter fw = null;
		fw = new FileWriter(filename, true);
		String endString = "<value>" + thisJoinPoint.getSignature().toLongString() + "||" 
								+ thisJoinPoint.getThis().toString() + "</value>\n";
		fw.write(endString);
		fw.close();
	}
	
}
