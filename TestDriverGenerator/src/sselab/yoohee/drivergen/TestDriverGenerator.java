package sselab.yoohee.drivergen;

import java.util.ArrayList;

import sselab.cadd.analyze.CodeAnt;
import sselab.cadd.cfg.Function;
import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.BinaryExpression;
import sselab.cadd.cfg.expression.type.CallExpression;
import sselab.cadd.cfg.expression.type.UnaryExpression;
import sselab.cadd.cfg.node.BranchStatement;
import sselab.cadd.cfg.node.Statement;
import sselab.cadd.cfg.node.TailStatement;
import sselab.cadd.variable.Variable;

/**
 * The class that get sliced source code
 * and control flow graph based on it
 * and get driver source code and print it out.
 * @version 2014-09-16
 * @author yoohee
 *
 */
public class TestDriverGenerator {
	
	private static final String inFileName = "input3.c";
	private static final String outFileName = "output3.c";
	//private static final String resultFileName = "testdriver.c";
	private static String code = "";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		//code analyzing
		CodeAnt ca = new CodeAnt();
		System.out.println("codeant start");
		ca.analyze(inFileName, outFileName); //analyze and get the sliced code
		ArrayList<Expression> expressions = new ArrayList<Expression>();
		ArrayList<Statement> statements = new ArrayList<Statement>();
		for(Function f : ca.getAnalyzeResult().getFunctions()){
			for(Statement s : f.getWhoCallThisCFG()){
				//get only 'sliced' statements
				if(s.isMarked()){
					gatherCalls(s.getExpression(), expressions);
					statements.add(s);
				}
			}	
		}
		DriverGenerator generator = new DriverGenerator(statements);
		code = generator.getCode();
		System.out.println(code);

	}
	
	private static void gatherCallsFromNodes(Statement s, ArrayList<Expression> expressions) {
		if ( s instanceof TailStatement )
			return ;
		
		if ( s instanceof BranchStatement ) {
			gatherCalls(((BranchStatement) s).getTrueNode().getExpression(), expressions);
			gatherCalls(((BranchStatement) s).getFalseNode().getExpression(), expressions);
			gatherCallsFromNodes(((BranchStatement) s).getTrueNode(), expressions);
			gatherCallsFromNodes(((BranchStatement) s).getFalseNode(), expressions);
		} else {
			gatherCalls(s.getExpression(), expressions);
			gatherCallsFromNodes(s.getNextNode(), expressions);
		}
	}
	
	private static void gatherCalls(Expression e, ArrayList<Expression> expressions) {
		if ( e instanceof CallExpression )
			expressions.add(e);
		else if ( e instanceof BinaryExpression ) {
			gatherCalls(((BinaryExpression) e).getOperand1(), expressions);
			gatherCalls(((BinaryExpression) e).getOperand2(), expressions);
		} else if ( e instanceof UnaryExpression ) {
			gatherCalls(e.getOperand(), expressions);
		} else {
			// do nothing for another things
		}	
	}
	

}
