package org.guidowb.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class PrintVisitor extends ASTVisitor {

	private final String bold = "\033[0;1m";
	private final String plain = "\033[0;0m";

	@Override
	public boolean visit(CompilationUnit unit) {
		System.out.println(bold + "package " + plain + unit.getPackage().getName());
		return true;
	}

	@Override
	public void preVisit(ASTNode node) {
		switch (node.getNodeType()) {
		case ASTNode.COMPILATION_UNIT:
			break;
		default:
			String nodeClass = node.getClass().getSimpleName();
			System.err.println(nodeClass + ": " + node.toString());
		}
	}
}
