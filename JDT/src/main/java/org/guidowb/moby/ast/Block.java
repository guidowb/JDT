package org.guidowb.moby.ast;

import java.util.ArrayList;
import java.util.List;

public class Block {

	private List<Statement> statements = new ArrayList<Statement>();
	
	public Block() {}
	public void add(Statement statement) { statements.add(statement); }
	public List<Statement> getStatements() { return statements; }
}
