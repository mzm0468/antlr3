/*
 * [The "BSD license"]
 *  Copyright (c) 2010 Terence Parr
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.antlr.tool;

import org.antlr.analysis.DecisionProbe;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/** More a single alternative recurses so this decision is not regular. */
public class NonRegularDecisionMessage extends Message {
	public DecisionProbe probe;
	public Set<Integer> altsWithRecursion;

	public NonRegularDecisionMessage(DecisionProbe probe, Set<Integer> altsWithRecursion) {
		super(ErrorManager.MSG_NONREGULAR_DECISION);
		this.probe = probe;
		this.altsWithRecursion = altsWithRecursion;
	}

	@Override
	public String toString() {
		GrammarAST decisionASTNode = probe.dfa.getDecisionASTNode();
		line = decisionASTNode.getLine();
		column = decisionASTNode.getCharPositionInLine();
		String fileName = probe.dfa.nfa.grammar.getFileName();
		if ( fileName!=null ) {
			file = fileName;
		}

		ST st = getMessageTemplate();
		String ruleName = probe.dfa.getNFADecisionStartState().enclosingRule.name;
		st.add("ruleName", ruleName);
		List sortedAlts = new ArrayList();
		sortedAlts.addAll(altsWithRecursion);
		Collections.sort(sortedAlts); // make sure it's 1, 2, ...
		st.add("alts", sortedAlts);

		return super.toString(st);
	}

}
