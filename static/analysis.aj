import java.io.*;

public aspect aspectjanalysictempfile {
	
	String filename = "analysisResult.xml";
	
	pointcut HelloWorldPointCut() : execution(* *(..));
	
	before() : HelloWorldPointCut(){
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(filename, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startString = "<start>" + thisJoinPoint.getSignature().toLongString() +"</start>\n";
		try {
			fw.write(startString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	after() : HelloWorldPointCut(){
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(filename, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String endString = "<end>" + thisJoinPoint.getSignature().toLongString() +"</end>\n";
		try {
			fw.write(endString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
