package org.to2mbn.authlibinjector.tweaker;

import static org.to2mbn.authlibinjector.AuthlibInjector.log;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class TweakerTransformerAdapter implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] classBuffer) {
		String internalClassName = name.replace('.', '/');
		for (ClassFileTransformer transformer : AuthlibInjectorTweaker.transformers) {
			byte[] result = null;
			try {
				result = transformer.transform(Launch.classLoader, internalClassName, null, null, classBuffer);
			} catch (IllegalClassFormatException e) {
				log("exception while invoking {0}: {1}", transformer, e);
				e.printStackTrace();
			}
			if (result != null) {
				classBuffer = result;
			}
		}
		return classBuffer;
	}

}
