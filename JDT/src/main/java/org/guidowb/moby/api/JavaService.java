package org.guidowb.moby.api;

import java.io.IOException;

import org.guidowb.moby.ast.Block;
import org.guidowb.moby.parsers.JavaParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JavaService {

	@RequestMapping(value="/api/java", method=RequestMethod.GET)
	public Block getJava() throws IOException {
		return JavaParser.parse("src/main/java/org/guidowb/moby/api/JavaService.java");
	}
}
