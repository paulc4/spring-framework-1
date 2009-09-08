/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.util;

import java.util.Properties;

/**
 * Utility class for working with Strings that have placeholder values in them. A placeholder takes the form
 * <code>${name}</code>. Using <code>PropertyPlaceholderUtils</code> these placeholders can be substituted for
 * user-supplied values. <p> Values for substitution can be supplied using a {@link Properties} instance or using a
 * {@link PlaceholderResolver}.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 3.0
 */
public class PropertyPlaceholderUtils {

	/** Prefix for property placeholders: "${" */
	public static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for property placeholders: "}" */
	public static final String PLACEHOLDER_SUFFIX = "}";

	/**
	 * Replaces all placeholders of format <code>${name}</code> with the corresponding property from the supplied {@link
	 * Properties}.
	 *
	 * @param value the value containing the placeholders to be replaced.
	 * @param properties the <code>Properties</code> to use for replacement.
	 * @return the supplied value with placeholders replaced inline.
	 */
	public static String replacePlaceholders(String value, final Properties properties) {
		Assert.notNull(properties, "Argument 'properties' must not be null.");
		return replacePlaceholders(value, new PlaceholderResolver() {

			public String resolvePlaceholder(String placeholderName) {
				return properties.getProperty(placeholderName);
			}
		});
	}

	/**
	 * Replaces all placeholders of format <code>${name}</code> with the value returned from the supplied {@link
	 * PlaceholderResolver}.
	 *
	 * @param value the value containing the placeholders to be replaced.
	 * @param placeholderResolver the <code>PlaceholderResolver</code> to use for replacement.
	 * @return the supplied value with placeholders replaced inline.
	 */
	public static String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
		StringBuilder result = new StringBuilder(value);

		int startIndex = result.indexOf(PLACEHOLDER_PREFIX);
		while (startIndex != -1) {
			int endIndex = result.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
			if (endIndex != -1) {
				String placeholder = result.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
				int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();

				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal != null) {
					result.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
					nextIndex = startIndex + propVal.length();
				}

				startIndex = result.indexOf(PLACEHOLDER_PREFIX, nextIndex);
			}
			else {
				startIndex = -1;
			}
		}

		return result.toString();
	}

	/**
	 * Strategy interface used to resolve replacement values for placeholders contained in Strings.
	 *
	 * @see org.springframework.util.PropertyPlaceholderUtils
	 */
	public static interface PlaceholderResolver {

		/**
		 * Resolves the supplied placeholder name into the replacement value.
		 *
		 * @param placeholderName the name of the placeholder to resolve.
		 * @return the replacement value or <code>null</code> if no replacement is to be made.
		 */
		String resolvePlaceholder(String placeholderName);
	}
}