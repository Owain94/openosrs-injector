/*
 * Copyright (c) 2020, Owain van Brakel <https://github.com/Owain94>
 * Copyright (c) 2019, Lucas <https://github.com/Lucwousin>
 * All rights reserved.
 *
 * This code is licensed under GPL3, see the complete license in
 * the LICENSE file in the root directory of this source tree.
 *
 * Copyright (c) 2018 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.openosrs.injector.injectors.raw;

import com.openosrs.injector.InjectUtil;
import com.openosrs.injector.Injexception;
import com.openosrs.injector.injection.InjectData;
import com.openosrs.injector.injectors.AbstractInjector;
import net.runelite.asm.ClassFile;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.instructions.InvokeSpecial;

public class FxApplet extends AbstractInjector
{
	public FxApplet(InjectData inject)
	{
		super(inject);
	}

	public void inject() throws Injexception
	{
		ClassFile deobGameShell = inject.getDeobfuscated().findClass("GameShell");
		String gameShellObName = InjectUtil.getObfuscatedName(deobGameShell);
		ClassFile vanillaGameShell = inject.getVanilla().findClass(gameShellObName);

		vanillaGameShell.setParentClass(new net.runelite.asm.pool.Class("javax/swing/JApplet"));

		for (Method method : vanillaGameShell.getMethods())
		{
			if ("<init>".equals(method.getName()))
			{
				for (Instruction instr : method.getCode().getInstructions())
				{
					if (instr instanceof InvokeSpecial)
					{
						InvokeSpecial invoke = (InvokeSpecial) instr;

						invoke.setMethod(
							new net.runelite.asm.pool.Method(
								new net.runelite.asm.pool.Class("javax/swing/JApplet"),
								invoke.getMethod().getName(),
								invoke.getMethod().getType()
							));

						break;
					}
				}
			}
		}
	}
}
