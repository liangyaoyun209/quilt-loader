/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.loader.impl.solver;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.quiltmc.loader.api.plugin.solver.LoadOption;
import org.quiltmc.loader.api.plugin.solver.ModLoadOption;
import org.quiltmc.loader.api.plugin.solver.ModSolveResult;
import org.quiltmc.loader.impl.discovery.ModCandidateCls;

public final class ModSolveResultImpl implements ModSolveResult {

	/** This doesn't include the provided mods. */
	public final Map<String, ModLoadOption> modMap;
	public final Map<String, ModLoadOption> providedMap;

	private final Map<Class<?>, LoadOptionResult<?>> extraResults;

	public ModSolveResultImpl(Map<String, ModLoadOption> modMap, Map<String, ModLoadOption> providedMap, Map<Class<?>, LoadOptionResult<?>> extraResults) {
		this.modMap = modMap;
		this.providedMap = providedMap;
		this.extraResults = extraResults;
	}

	@Override
	public <O> LoadOptionResult<O> getResult(Class<O> optionClass) {
		LoadOptionResult<?> result = extraResults.get(optionClass);
		if (result == null) {
			return new LoadOptionResult<>(Collections.emptyMap());
		}
		return (LoadOptionResult<O>) result;
	}

	public static final class LoadOptionResult<O> implements SpecificLoadOptionResult<O> {
		private final Map<O, Boolean> result;

		public LoadOptionResult(Map<O, Boolean> result) {
			this.result = result;
		}

		@Override
		public Collection<O> getOptions() {
			return result.keySet();
		}

		@Override
		public boolean isPresent(O option) {
			return result.getOrDefault(option, false);
		}
	}
}
