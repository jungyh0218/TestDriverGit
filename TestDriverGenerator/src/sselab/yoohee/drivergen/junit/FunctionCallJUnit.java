package sselab.yoohee.drivergen.junit;

import java.util.ArrayList;

import org.junit.Test;

import sselab.cadd.analyze.CodeAnt;
import sselab.cadd.cfg.Function;
import sselab.cadd.cfg.expression.Expression;
import sselab.cadd.cfg.expression.type.BinaryExpression;
import sselab.cadd.cfg.expression.type.CallExpression;
import sselab.cadd.cfg.expression.type.UnaryExpression;
import sselab.cadd.cfg.node.Statement;
import sselab.yoohee.drivergen.ConcatExpr;
import sselab.yoohee.drivergen.FunctionCall;

public class FunctionCallJUnit {
	private final String inFileName = "test.c";
	private final String outFileName = "output.c";
	private CodeAnt ca = new CodeAnt();
	private ArrayList<Expression> expressions = new ArrayList<Expression>();
	private ArrayList<Statement> statements = new ArrayList<Statement>();
	
	@Test
	public void testConcatConstraints(){
		initialize();
		FunctionCall fc = new FunctionCall(statements.get(0));
		System.out.println(fc.callExpr.getRawString());
		fc.gatherConstraints();
		ConcatExpr ce = fc.concatConstraints();
		System.out.println(ce.getCode());
	}

	private void initialize(){
		ca.analyze(inFileName, outFileName);
		
		for(Function f : ca.getAnalyzeResult().getFunctions()){
			for(Statement s : f.getWhoCallThisCFG()){
				gatherCalls(s.getExpression(), expressions);
				statements.add(s);
			}
			
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
