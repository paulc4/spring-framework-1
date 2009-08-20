/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.model.binder.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.alert.Severity;
import org.springframework.model.binder.BindingResult;
import org.springframework.model.binder.BindingResults;

class ArrayListBindingResults implements BindingResults {

	private List<BindingResult> results;

	public ArrayListBindingResults() {
		results = new ArrayList<BindingResult>();
	}

	public ArrayListBindingResults(int size) {
		results = new ArrayList<BindingResult>(size);
	}

	public void add(BindingResult result) {
		results.add(result);
	}

	// implementing Iterable

	public Iterator<BindingResult> iterator() {
		return results.iterator();
	}

	// implementing BindingResults

	public List<BindingResult> successes() {
		List<BindingResult> results = new ArrayList<BindingResult>();
		for (BindingResult result : this) {
			if (!result.isFailure()) {
				results.add(result);
			}
		}
		return results;
	}

	public List<BindingResult> failures() {
		List<BindingResult> results = new ArrayList<BindingResult>();
		for (BindingResult result : this) {
			if (result.isFailure()) {
				results.add(result);
			}
		}
		return results;
	}

	public boolean hasErrors() {
		return errors().size() > 0;
	}
	
	public List<BindingResult> errors() {
		List<BindingResult> results = new ArrayList<BindingResult>();
		for (BindingResult result : this) {
			if (result.isFailure() && result.getAlert().getSeverity().compareTo(Severity.ERROR) >= 0) {
				results.add(result);
			}
		}
		return results;
	}

	public BindingResult get(int index) {
		return results.get(index);
	}
	
	public BindingResult get(String fieldName) {
		for (BindingResult result : results) {
			if (result.getFieldName().equals(fieldName)) {
				return result;
			}
		}
		return null;
	}

	public int size() {
		return results.size();
	}

	public String toString() {
		return "[BindingResults = " + results.toString() + "]";
	}
}