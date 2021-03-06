/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.metrics.graphql;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

public class DefaultGraphQLTagsProvider implements GraphQLTagsProvider {

	private static final Tag OUTCOME_SUCCESS = Tag.of("outcome", "SUCCESS");

	private static final Tag OUTCOME_ERROR = Tag.of("outcome", "ERROR");


	@Override
	public Iterable<Tag> getTags(InstrumentationExecutionParameters parameters, ExecutionResult result, Throwable exception) {
		Tags tags = Tags.of(Tag.of("query", parameters.getQuery()));
		if (result.isDataPresent()) {
			tags = tags.and(OUTCOME_SUCCESS);
		}
		else {
			tags = tags.and(OUTCOME_ERROR);
			if (!result.getErrors().isEmpty()) {
				ErrorClassification errorClassification = result.getErrors().get(0).getErrorType();
				if (errorClassification instanceof ErrorType) {
					tags = tags.and(Tag.of("errorType", ((ErrorType) errorClassification).name()));
				}
			}
		}
		return tags;
	}
}
