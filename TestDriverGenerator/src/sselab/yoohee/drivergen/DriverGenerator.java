package sselab.yoohee.drivergen;

import java.util.ArrayList;

public class DriverGenerator {
	private String code = "";
	ArrayList<FunctionCall> calls = new ArrayList<FunctionCall>();
	
	public String getCode(){
		code += "void testDriver()"
				+ "{";
		addVarSymbolization();
		addRestrictions();
		addConstraints();
		callTargetFunction();
		code += "}\n";
		return code;
	}
	
	private void addVarSymbolization(){
		//add initialization
		//symbolize them (CREST_int(varName);)
	}
	
	private String addRestrictions(){
		String expString= "";
		//add global variable restriction
		return expString;
	}
	private String addConstraints(){
		String expString= "";
		expString += "assert(";
		for(FunctionCall n: calls){
			expString += n.getCode();
			if(calls.indexOf(n) < calls.size()-1)
				expString += " || ";
		}
		expString += ")\n";
		return expString;
		//add parameter constraints
	}
	private void callTargetFunction(){
		// functionName(symbolized_params);
	}
	
	
	
}
