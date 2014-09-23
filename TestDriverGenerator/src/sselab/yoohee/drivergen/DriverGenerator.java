package sselab.yoohee.drivergen;

import java.util.ArrayList;

import sselab.cadd.cfg.expression.type.CallExpression;
import sselab.cadd.cfg.node.Statement;

/**
 * DriverGenerator class
 * This class generates whole code of a test driver.
 * @version 2014-09-16
 * @author yoohee
 * 
 *
 */
public class DriverGenerator {
	private String code = "";
	public ArrayList<FunctionCall> calls = new ArrayList<FunctionCall>();
	
	public DriverGenerator(ArrayList<Statement> _statements){
		for(Statement s : _statements){
			calls.add(new FunctionCall(s));
		}
	}
	
	public String getCode(){
		code += "void testDriver()"
				+ "{";
		code += addVarSymbolization();
		code += addRestrictions();
		code += addConstraints();
		callTargetFunction();
		code += "}\n";
		return code;
	}
	
	private String addVarSymbolization(){
		String expString = "";
		//add initialization
		//symbolize them (CREST_int(varName);)
		return expString;
	}
	
	private String addRestrictions(){
		String expString= "";
		expString += "assert(";
		//add global variable restriction
		expString += ")\n";
		return expString;
	}
	private String addConstraints(){
		String expString= "";
		expString += "assert(";
		for(FunctionCall n: calls){
			expString += "(" + n.getCode() + ")";
			if(calls.indexOf(n) < calls.size()-1)
				expString += " || ";
		}
		expString += ");\n";
		return expString;
		//add parameter constraints
	}
	private void callTargetFunction(){
		// functionName(symbolized_params);
	}
	
	
	
}
