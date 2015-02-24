package org.guidowb.moby.api;

import java.io.IOException;

import org.guidowb.moby.ast.Block;
import org.guidowb.moby.parsers.JavaParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JavaService {

	@RequestMapping(value="/api/java", method=RequestMethod.GET, params={"file"})
	public Block getJava(@RequestParam String file) throws IOException {
		return JavaParser.parse(file);
	}
}
