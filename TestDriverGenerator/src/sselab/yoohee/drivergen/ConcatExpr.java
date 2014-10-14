package sselab.yoohee.drivergen;

import java.util.ArrayList;

import sselab.cadd.cfg.expression.Expression;
/**
 * @version 2014-10-14
 * @author yoohee
 *
 */
public class ConcatExpr extends Expr{
	Expr lchild = null;
	String operator = "";
	Expr rchild = null;
	
	public ConcatExpr(TerminalExpr terminal){
		this.lchild = terminal;
	}
	public ConcatExpr(TerminalExpr l, String op, ConcatExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public ConcatExpr(ConcatExpr l, String op, ConcatExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public ConcatExpr(ConcatExpr l, String op, TerminalExpr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public ConcatExpr(Expr terminal){
		this.lchild = terminal;
	}
	public ConcatExpr(Expr l, String op, Expr r){
		this.lchild = l;
		this.operator = op;
		this.rchild = r;
	}
	public String getCode(){
		String code = "";
		code += lchild.getCode();
		if(rchild != null){
		code += " " + operator + " ";
		code += rchild.getCode();
		}
		return code;
	}

	
	
	
	
}
